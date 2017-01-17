package it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CustomLayoutTestApp.class)
public class LayoutTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Test
    public void usesDefaultLayout() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
               .andDo(print())
               .andExpect(xpath("/html/head/title").string("I am default layout"))
               .andExpect(xpath("/html/body/h1").string("Header in default layout!"))
               .andExpect(xpath("/html/body/nav").string("Nav"))
               .andExpect(xpath("/html/body/main/p").string("Using default layout"))
               .andExpect(xpath("/html/body/footer").string("Footer"));
    }

    @Test
    public void usesSimpleLayout() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/simple"))
               .andDo(print())
               .andExpect(xpath("/html/head/title").string("I am simple layout"))
               .andExpect(xpath("/html/body/h1").string("Header in simple layout!"))
               .andExpect(xpath("/html/body/nav").doesNotExist())
               .andExpect(xpath("/html/body/main/p").string("Using simple layout"))
               .andExpect(xpath("/html/body/footer").doesNotExist());
    }

    @Test
    public void usesNoLayout() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/no-layout"))
               .andDo(print())
               .andExpect(xpath("/html/head/title").string("No layout!"));
    }

    @Test
    public void parameterizableViewControllerHasDefaultLayout() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/vc"))
               .andDo(print())
               .andExpect(xpath("/html/head/title").string("I am default layout"));
    }
}
