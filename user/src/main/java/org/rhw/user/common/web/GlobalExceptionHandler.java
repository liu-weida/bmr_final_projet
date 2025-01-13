package org.rhw.user.common.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.rhw.user.common.convention.errorcode.BaseErrorCode;
import org.rhw.user.common.convention.exception.AbstractException;
import org.rhw.user.common.convention.result.Result;
import org.rhw.user.common.convention.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.Optional;

/**
 * 全局异常处理器
 * 注册为spring组件即可生效，不需要显式调用
 * */
// 声明为 Spring 的组件，命名为 "globalExceptionHandlerByAdmin"（可被其他地方引用）
@Component("globalExceptionHandlerByAdmin")
// 使用 Lombok 提供的注解，启用日志记录功能
@Slf4j
// 声明为全局异常处理器，支持捕获 Controller 层的异常
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数验证失败的异常
     * 捕获 `@Valid` 或 `@Validated` 注解校验失败抛出的异常 `MethodArgumentNotValidException`
     */
    @SneakyThrows // Lombok 注解，抑制检查型异常的编译错误（不需要显式声明 throws）
    @ExceptionHandler(value = MethodArgumentNotValidException.class) // 声明处理的异常类型
    public Result validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        // 获取校验失败的绑定结果
        BindingResult bindingResult = ex.getBindingResult();
        // 获取第一个字段错误
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        // 获取错误消息，若不存在则返回空字符串
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        // 打印错误日志，包括请求方法、请求 URL 和错误信息
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr);
        // 返回失败结果，使用通用的错误码和错误信息
        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
    }

    /**
     * 处理应用内自定义异常
     * 捕获继承自 AbstractException 的所有异常
     */
    @ExceptionHandler(value = {AbstractException.class}) // 声明处理 AbstractException 类型的异常
    public Result abstractException(HttpServletRequest request, AbstractException ex) {
        // 如果异常包含原始的触发原因
        if (ex.getCause() != null) {
            // 打印错误日志，包括请求方法、请求 URL 和异常的详细信息
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString(), ex.getCause());
            // 返回失败结果，包含异常中的错误码和错误信息
            return Results.failure(ex);
        }
        // 如果没有原始异常，仅打印简单错误信息
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        // 返回失败结果
        return Results.failure(ex);
    }

    /**
     * 处理未捕获的所有异常
     * 捕获除已处理异常以外的所有异常
     */
    @ExceptionHandler(value = Throwable.class) // 声明处理所有 Throwable 类型的异常
    public Result defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        // 打印错误日志，包括请求方法、请求 URL 和异常堆栈
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        // 针对自定义异常的聚合模式，判断当前异常是否继承自 AbstractException
        if (Objects.equals(throwable.getClass().getSuperclass().getSimpleName(), AbstractException.class.getSimpleName())) {
            // 使用反射获取异常中的错误码
            String errorCode = ReflectUtil.getFieldValue(throwable, "errorCode").toString();
            // 使用反射获取异常中的错误消息
            String errorMessage = ReflectUtil.getFieldValue(throwable, "errorMessage").toString();
            // 返回失败结果，包含错误码和错误消息
            return Results.failure(errorCode, errorMessage);
        }
        // 对于其他类型的异常，返回通用失败结果
        return Results.failure();
    }

    /**
     * 获取请求的完整 URL（包含查询参数）
     */
    private String getUrl(HttpServletRequest request) {
        // 如果请求没有查询参数，则返回基本的 URL
        if (StringUtils.isEmpty(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        // 否则返回完整的 URL（基本 URL + 查询参数）
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
