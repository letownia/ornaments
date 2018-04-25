package com.lojasiewicz.mapDecorator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class MapDecoratorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${areTestPropertiesBeingUsed}")
    private String areTestPropertiesBeingUsed;

    @Before
    public void setup(){
        assertThat(areTestPropertiesBeingUsed).isNotNull();
    }

    /**
     *
     * See https://docs.spring.io/spring-security/site/docs/current/reference/html/test-mockmvc.html
     *
     * For better testing of web-security (valid headers, valid users, valid tokens, etc. )
     *
     * @throws Exception
     */
    @Test
    public void testIndexReturnsProperly() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("</html>")));
    }
}