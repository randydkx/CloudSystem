package cn.edu.njust.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

//    对请求进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url=request.getRequestURI();
        return true;
//        if(!(url.toLowerCase().contains("login"))){
//            HttpSession session = request.getSession();
//
//            if(session.getAttribute("user")!=null  || session.getAttribute("rest")!=null ||
//                    session.getAttribute("manager")!=null)
//                return true;
//            else{
//                System.out.println("LoginInterceptor->转移url->"+request.getContextPath()+"/user/toLogin.do");
//                response.sendRedirect(request.getContextPath()+"/user/toLogin.do");
//            }
//        }else{
//            return true;
//        }
//        return false;//默认情况下拦截
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
