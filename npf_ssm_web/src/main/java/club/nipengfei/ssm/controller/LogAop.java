package club.nipengfei.ssm.controller;

import club.nipengfei.ssm.domain.SysLog;
import club.nipengfei.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    private Date visitTime; // 开始时间
    private Class clazz;  // 访问的类
    private Method method; // 访问的方法

    // 前置通知 主要获取开始时间，执行的类哪一个，执行的哪一个方法
    @Before("execution(* club.nipengfei.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime = new Date();  // 当前时间就是开始访问的类
        clazz = jp.getTarget().getClass(); // 具体访问的类
        String methodName = jp.getSignature().getName();  // 获取访问方法名称
        Object[] args = jp.getArgs();  // 获取访问方法参数

        // 获取具体执行方法Method对象
        if (args==null || args.length==0){
            method =  clazz.getMethod(methodName);
        } else {
            Class[] classArgs = new Class[args.length];
            for (int i=0;i<args.length;i++){
                classArgs[i] = args[i].getClass();
            }
            method = clazz.getMethod(methodName,classArgs);
        }
    }

    // 后置通知
    @After("execution(* club.nipengfei.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {

        // 获取访问时长
        long time = new Date().getTime()-visitTime.getTime();

        String url = "";
        // 获取url
        if (clazz != null && method !=null && clazz!=LogAop.class){
            // 获取类的@RequestMapping("/orders")
            RequestMapping clazzAnnotation =(RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (clazzAnnotation != null){
                String[] classValue = clazzAnnotation.value();
                // 获取方法上的@RequestMapping("xxx")
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null){
                    String[] methodValue = methodAnnotation.value();
                    url=classValue[0]+methodValue[0];

                    // 获取IP地址
                    String ip = request.getRemoteAddr();

                    // 获取当前操作的用户
                    SecurityContext context = SecurityContextHolder.getContext();
                    User user =(User) context.getAuthentication().getPrincipal();
                    String username = user.getUsername();

                    // 将日志相关信息封装到SysLog对象
                    SysLog sysLog = new SysLog();
                    sysLog.setExecutionTime(time);
                    sysLog.setIp(ip);
                    sysLog.setMethod("[类名] "+clazz.getName()+"[方法名] "+method.getName());
                    sysLog.setUrl(url);
                    sysLog.setUsername(username);
                    sysLog.setVisitTime(visitTime);

                    // 调用service完成操作
                    sysLogService.save(sysLog);
                }
            }

        }
    }
}
