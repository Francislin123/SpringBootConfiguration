package test.java.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.walmart.entity.PartnersEntity;

/*** Created by Francislin Dos Reis on 11/08/17.*/

public class PartnersRestApiControllerTemplate implements TemplateLoader {

    public static final String PARTNERS_CREATE_REQUEST = "create_partners_request";
    public static final String PARTNERS_LIST_REQUEST = "list_partners_request";
    public static final String PARTNERS_UPDATE_REQUEST = "partner_update_request";
    public static final String PARTNERS_CREATE_CONFLICT_REQUEST = "partner_create_conflict_request";

    @Override
    public void load() {

        Fixture.of(PartnersEntity.class).addTemplate(PARTNERS_CREATE_REQUEST, new Rule() {{
            add("id", "1");
            add("partnersName", "Facebook");
            add("productName", "Product facebook test");
        }});

        Fixture.of(PartnersEntity.class).addTemplate(PARTNERS_CREATE_CONFLICT_REQUEST, new Rule() {{
            add("id", "1");
            add("partnersName", "Google");
            add("productName", "Product test");
        }});

        Fixture.of(PartnersEntity.class).addTemplate(PARTNERS_LIST_REQUEST, new Rule() {{
            add("partnersName", "Facebook");
            add("productName", "Product test");
        }});

        Fixture.of(PartnersEntity.class).addTemplate(PARTNERS_UPDATE_REQUEST, new Rule() {{
            add("partnersName", "Facebook");
            add("productName", "Text description");
        }});
    }
}
