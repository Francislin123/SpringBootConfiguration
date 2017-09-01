package test;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.walmart.controllers.PartnersRestApiController;
import br.com.walmart.entity.PartnersEntity;
import br.com.walmart.service.PartnersService;
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
public class PartnersRestApiControllerTest {

    public static final String V1_PARTNERS = "/v1/partnersEntity/create/";
    public static final String V1_PARTNERS_LIST = "/v1/partnersEntity/list/";
    public static final String V1_PARTNERS_UPDATE = "/v1/partnersEntity/find/";
    public static final String V1_PARTNERS_DELETE = "/v1/partnersEntity/delete/";

    public static final String PARTNERS_CREATE_REQUEST = "create_partners_request";
    public static final String PARTNERS_UPDATE_REQUEST = "partner_update_request";

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private PartnersEntity partnersEntity;

    @Mock
    private PartnersService partnersService;

    @InjectMocks
    private PartnersRestApiController partnersRestApiController;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("test.java.template");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(partnersRestApiController).build();
        mapper = new ObjectMapper();
        partnersEntity = new PartnersEntity();
    }

    //--------------------------------- Create partner test ----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testCreatePartnersSuccess() {
        mockMvc.perform(post(V1_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(Fixture.from(PartnersEntity.class).gimme(PARTNERS_CREATE_REQUEST))))
                .andExpect(status().isCreated());
        verify(partnersService).savePartners(any(PartnersEntity.class));
    }

    @Test
    @SneakyThrows
    public void testCreatePartnersConflict() {
        partnersEntity.setId(1);
        partnersEntity.setPartnersName("Google");
        partnersEntity.setProductName("google");
        when(partnersService.isPartnersExist(partnersEntity)).thenReturn(Boolean.TRUE);
        mockMvc.perform(post(V1_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(partnersEntity)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
        verify(partnersService, times(1)).isPartnersExist(partnersEntity);
        verify(partnersService, times(0)).savePartners(any(PartnersEntity.class));
    }

    //--------------------------------- Find partner by id test ------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testFindByPartnersIdSuccess() {
        partnersEntity.setId(1);
        when(partnersService.findById(partnersEntity.getId())).thenReturn(partnersEntity);
        mockMvc.perform(MockMvcRequestBuilders.get(V1_PARTNERS_UPDATE + partnersEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(partnersEntity.getId()))
                .content(jsonRequest(Fixture.from(PartnersEntity.class).gimme(PARTNERS_CREATE_REQUEST))))
                .andExpect(status().isOk());
        verify(partnersService, times(1)).findById(partnersEntity.getId());
        verifyNoMoreInteractions(partnersService);
    }

    @Test
    @SneakyThrows
    public void testFindByPartnersIdNotFound() {
        partnersEntity.setId(1);
        when(partnersService.findById(partnersEntity.getId())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get(V1_PARTNERS_UPDATE + partnersEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound());
        verify(partnersService, times(1)).findById(partnersEntity.getId());
        verifyNoMoreInteractions(partnersService);
    }

    //--------------------------------- List partner test ------------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testListPartnersOk() {
        mockMvc.perform(MockMvcRequestBuilders.get(V1_PARTNERS_LIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
        verify(partnersService).findAllPartners();
    }

    //--------------------------------- Update partner test ----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testUpdatePartnersByIdSuccess() {
        partnersEntity.setId(1);
        when(partnersService.findById(partnersEntity.getId())).thenReturn(partnersEntity);
        doNothing().when(partnersService).updatePartners(partnersEntity);
        mockMvc.perform(put(V1_PARTNERS_UPDATE + partnersEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(Fixture.from(PartnersEntity.class).gimme(PARTNERS_UPDATE_REQUEST))))
                .andExpect(status().isOk());
        verify(partnersService, times(1)).findById(partnersEntity.getId());
        verify(partnersService, times(1)).updatePartners(partnersEntity);
    }

    @Test
    @SneakyThrows
    public void testUpdatePartnersNotFound() {
        mockMvc.perform(put(V1_PARTNERS_UPDATE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRequest(Fixture.from(PartnersEntity.class).gimme(PARTNERS_UPDATE_REQUEST))))
                .andExpect(status().isNotFound());
        verifyNoMoreInteractions(partnersService);
    }

    //--------------------------------- Delete partner test ----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testDeletePartnersOk() {
        partnersEntity.setId(1);
        partnersEntity.setPartnersName("Facebook");
        partnersEntity.setProductName("facebook");
        when(partnersService.findById(partnersEntity.getId())).thenReturn(partnersEntity);
        mockMvc.perform(delete(V1_PARTNERS_DELETE + partnersEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
        verify(partnersService, times(0)).findById(partnersEntity.getId());
    }

    @Test
    @SneakyThrows
    public void testDeletePartnersNonExistent() {
        doThrow(NullPointerException.class).when(partnersService).delete(partnersEntity.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .delete(PartnersRestApiController.V1_PARTNERS_DELETE + partnersEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //--------------------------------- Object Json  -----------------------------------------------------------------//

    @SneakyThrows
    public String jsonRequest(Object request) {
        return mapper.writeValueAsString(request);
    }
}
