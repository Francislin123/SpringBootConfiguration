package test;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.walmart.controllers.HomeController;
import br.com.walmart.entity.Partners;
import br.com.walmart.service.PartnersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by vn0gshm on 11/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

    public static final String V1_PARTNERS = "/v1/partners/create/";
    public static final String V1_PARTNERS_LIST = "/v1/partners/list/";
    public static final String V1_PARTNERS_UPDATE = "/v1/partners/find/";
    public static final String V1_PARTNERS_DELETE = "/v1/partners/delete/";

    public static final String PARTNERS_CREATE_REQUEST = "create_partners_request";
    public static final String PARTNERS_UPDATE_REQUEST = "partner_update_request";

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private Partners partners;

    @Mock
    private PartnersService partnersService;

    @InjectMocks
    private HomeController homeController;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("test.java.template");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
        mapper = new ObjectMapper();
        partners = new Partners();
    }

    //--------------------------------- Create partner test ----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testCreatePartnersSuccess(){
        mockMvc.perform(post(V1_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(Fixture.from(Partners.class).gimme(PARTNERS_CREATE_REQUEST))))
                .andExpect(status().isCreated());
        verify(partnersService).savePartners(any(Partners.class));
    }

    @Test
    @SneakyThrows
    public void testCreatePartnersConflict(){
        partners.setId(1);
        partners.setPartnersName("Google");
        partners.setProductName("google");
        when(partnersService.isPartnersExist(partners)).thenReturn(Boolean.TRUE);
        mockMvc.perform(post(V1_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(partners)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
        verify(partnersService, times(1)).isPartnersExist(partners);
        verify(partnersService, times(0)).savePartners(any(Partners.class));
    }

    //--------------------------------- Find partner by id test ------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testFindByPartnersIdSuccess(){
        partners.setId(1);
        when(partnersService.findById(partners.getId())).thenReturn(partners);
        mockMvc.perform(MockMvcRequestBuilders.get(V1_PARTNERS_UPDATE + partners.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(partners.getId()))
                .content(jsonRequest(Fixture.from(Partners.class).gimme(PARTNERS_CREATE_REQUEST))))
                .andExpect(status().isOk());
        verify(partnersService, times(1)).findById(partners.getId());
        verifyNoMoreInteractions(partnersService);
    }

    @Test
    @SneakyThrows
    public void testFindByPartnersIdNotFound(){
        partners.setId(1);
        when(partnersService.findById(partners.getId())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get(V1_PARTNERS_UPDATE + partners.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
        verify(partnersService, times(1)).findById(partners.getId());
        verifyNoMoreInteractions(partnersService);
    }

    //--------------------------------- List partner test ------------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testListPartners(){
        mockMvc.perform(MockMvcRequestBuilders.get(V1_PARTNERS_LIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
        verify(partnersService).findAllPartners();
    }

    //--------------------------------- Update partner test ----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testUpdatePartnersByIdSuccess(){
        partners.setId(1);
        when(partnersService.findById(partners.getId())).thenReturn(partners);
        doNothing().when(partnersService).updatePartners(partners);
        mockMvc.perform(put(V1_PARTNERS_UPDATE + partners.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(Fixture.from(Partners.class).gimme(PARTNERS_UPDATE_REQUEST))))
                .andExpect(status().isOk());
        verify(partnersService, times(1)).findById(partners.getId());
        verify(partnersService, times(1)).updatePartners(partners);
    }

    @Test
    @SneakyThrows
    public void testUpdatePartnersNotFound(){
        mockMvc.perform(put(V1_PARTNERS_UPDATE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(Fixture.from(Partners.class).gimme(PARTNERS_UPDATE_REQUEST))))
                .andExpect(status().isNotFound());
        verifyNoMoreInteractions(partnersService);
    }

    //--------------------------------- Delete partner test ----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testDeletePartnersOk(){
        partners.setId(1);
        partners.setPartnersName("Facebook");
        partners.setProductName("facebook");
        when(partnersService.findById(partners.getId())).thenReturn(partners);
        mockMvc.perform(delete(V1_PARTNERS_DELETE + partners.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
        verify(partnersService, times(0)).findById(partners.getId());
    }

    @Test
    @SneakyThrows
    public void testDeletePartnersNonExistent(){
        doThrow(NullPointerException.class).when(partnersService).delete(partners.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .delete(HomeController.V1_PARTNERS_DELETE + partners.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //--------------------------------- Object Json  -----------------------------------------------------------------//

    @SneakyThrows
    public String jsonRequest(Object request){
        return mapper.writeValueAsString(request);
    }
}
