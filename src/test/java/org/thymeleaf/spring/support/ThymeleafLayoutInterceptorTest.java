package org.thymeleaf.spring.support;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ThymeleafLayoutInterceptorTest {
    private ThymeleafLayoutInterceptor interceptor;
    @Mock
    private ModelAndView modelAndView;
    @Mock
    private HandlerMethod handlerMethod;

    @Before
    public void before() {
        interceptor = new ThymeleafLayoutInterceptor();
        interceptor.setDefaultLayout("someLayout");
        interceptor.setViewAttributeName("attributeName");
    }

    @Test
    public void returnsWhenModelAndViewIsNull() throws Exception {
        interceptor.postHandle(null, null, null, null);
    }

    @Test
    public void returnsWhenNoViewIsPresent() throws Exception {
        // arrange
        when(modelAndView.hasView()).thenReturn(false);
        // act
        interceptor.postHandle(null, null, null, modelAndView);
        // assert
        verify(modelAndView).hasView();
        verifyNoMoreInteractions(modelAndView);
    }

    @Test
    public void returnsWhenRedirect() throws Exception {
        // arrange
        when(modelAndView.hasView()).thenReturn(true);
        when(modelAndView.getViewName()).thenReturn("redirect:/");
        // act
        interceptor.postHandle(null, null, null, modelAndView);
        // assert
        verify(modelAndView).hasView();
        verify(modelAndView).getViewName();
        verifyNoMoreInteractions(modelAndView);
    }

    @Test
    public void returnsWhenForward() throws Exception {
        // arrange
        when(modelAndView.hasView()).thenReturn(true);
        when(modelAndView.getViewName()).thenReturn("forward:/");
        // act
        interceptor.postHandle(null, null, null, modelAndView);
        // assert
        verify(modelAndView).hasView();
        verify(modelAndView).getViewName();
        verifyNoMoreInteractions(modelAndView);
    }

    @Test
    public void layoutBecomesView() throws Exception {
        // arrange
        when(modelAndView.hasView()).thenReturn(true);
        when(modelAndView.getViewName()).thenReturn("someView");

        when(handlerMethod.getMethodAnnotation(Layout.class)).thenReturn(null);
        Mockito.<Class<?>>when(handlerMethod.getBeanType()).thenReturn(Object.class);
        // act
        interceptor.postHandle(null, null, handlerMethod, modelAndView);
        // assert
        verify(modelAndView).setViewName("someLayout");
        verify(modelAndView).addObject("attributeName", "someView");
    }

    @Test
    public void layoutChangesWhenAnnotationIsPresentOnAHandlerMethod() throws Exception {
        // arrange
        when(modelAndView.hasView()).thenReturn(true);
        when(modelAndView.getViewName()).thenReturn("someView");

        Layout layout = layoutAnnotation();
        when(handlerMethod.getMethodAnnotation(Layout.class)).thenReturn(layout);

        // act
        interceptor.postHandle(null, null, handlerMethod, modelAndView);
        // assert
        verify(modelAndView).setViewName("newLayout");
        verify(modelAndView).addObject("attributeName", "someView");
    }

    @Test
    public void layoutChangesWhenAnnotationIsPresentOnAHandlerClass() throws Exception {
        // arrange
        when(modelAndView.hasView()).thenReturn(true);
        when(modelAndView.getViewName()).thenReturn("someView");

        Layout layout = layoutAnnotation();
        when(handlerMethod.getMethodAnnotation(Layout.class)).thenReturn(null);
        when(handlerMethod.getMethodAnnotation(Layout.class)).thenReturn(layout);
        // act
        interceptor.postHandle(null, null, handlerMethod, modelAndView);
        // assert
        verify(modelAndView).setViewName("newLayout");
        verify(modelAndView).addObject("attributeName", "someView");
    }

    private static Layout layoutAnnotation() {
        return AnnotatedHandler.class.getAnnotation(Layout.class);
    }

    @Layout(value = "newLayout")
    private static final class AnnotatedHandler  {}
}
