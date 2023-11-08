package com.zznode.dhmp.data.exception;

import com.zznode.dhmp.core.constant.BaseConstants;
import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.core.exception.OptimisticLockException;
import com.zznode.dhmp.core.message.DhmpMessageSource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLException;
import java.util.Optional;

import static com.zznode.dhmp.data.constant.CustomHeaders.HAS_ERROR;

/**
 * 全局异常处理器
 *
 * @author 王俊
 * @date create in 2023/6/28 10:22
 */
@ControllerAdvice
public class GlobalExceptionHandler implements MessageSourceAware {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    MessageSourceAccessor messages = DhmpMessageSource.getAccessor();


    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, CommonException exception) {
        if (logger.isWarnEnabled()) {
            logger.warn(exceptionMessage("Common exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage(exception.getCode(), exception.getParameters()));
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截处理 Valid 异常
     *
     * @param exception 异常
     * @return ExceptionResponse
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request,  MultipartException exception) {
        if (logger.isInfoEnabled()) {
            logger.info(exceptionMessage("Multipart exception", request, null), exception);
        }
        String defaultMessage = exception.getMessage();
        ExceptionResponse er = new ExceptionResponse(defaultMessage);
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截处理 Valid 异常
     *
     * @param exception 异常
     * @return ExceptionResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, MethodArgumentNotValidException exception) {
        if (logger.isInfoEnabled()) {
            logger.info(exceptionMessage("Method arg invalid exception", request, method), exception);
        }
        String defaultMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ExceptionResponse er = new ExceptionResponse(defaultMessage);
        setDevException(er, exception);
        return errorResponse(er);
    }


    /**
     * 拦截处理 DuplicateKeyException 异常
     * 主键重复
     *
     * @param exception DuplicateKeyException
     * @return ExceptionResponse
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, DuplicateKeyException exception) {
        if (logger.isWarnEnabled()) {
            logger.warn(exceptionMessage("Duplicate key exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage("error.db.duplicateKey"));
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截处理 BadSqlGrammarException 异常
     * 搜索接口排序字段错误，在这里拦截异常，并友好返回前端
     *
     * @param exception BadSqlGrammarException
     * @return ExceptionResponse
     */
    @ExceptionHandler(org.springframework.jdbc.BadSqlGrammarException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, BadSqlGrammarException exception) {
        if (logger.isErrorEnabled()) {
            logger.error(exceptionMessage("Bad sql exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage("error.db.badSql"));
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截 {@link IllegalArgumentException} 异常信息，返回 “数据校验不通过” 信息
     *
     * @param exception IllegalArgumentException
     * @return ExceptionResponse
     */
    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, RuntimeException exception) {
        if (logger.isErrorEnabled()) {
            logger.error(exceptionMessage("Illegal argument exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage(BaseConstants.ErrorCode.DATA_INVALID));
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截 {@link OptimisticLockException} 异常信息，返回 “记录不存在或版本不一致” 信息
     *
     * @param exception OptimisticLockException
     * @return ExceptionResponse
     */
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, OptimisticLockException exception) {
        if (logger.isWarnEnabled()) {
            logger.warn(exceptionMessage("Optimistic lock exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage(BaseConstants.ErrorCode.OPTIMISTIC_LOCK));
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截 {@link RuntimeException} / {@link Exception} 异常信息，返回 “程序出现错误，请联系管理员” 信息
     *
     * @param exception 异常
     * @return ExceptionResponse
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, Exception exception) {
        if (logger.isErrorEnabled()) {
            logger.error(exceptionMessage("runtime exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage(BaseConstants.ErrorCode.ERROR));
        setDevException(er, exception);
        return errorResponse(er);
    }

    /**
     * 拦截 {@link SQLException} 异常信息，返回 “数据操作错误，请联系管理员” 信息
     *
     * @param exception 异常
     * @return ExceptionResponse
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponse> process(HttpServletRequest request, HandlerMethod method, SQLException exception) {
        if (logger.isErrorEnabled()) {
            logger.error(exceptionMessage("Sql exception", request, method), exception);
        }
        ExceptionResponse er = new ExceptionResponse(translateMessage(BaseConstants.ErrorCode.ERROR_SQL_EXCEPTION));
        setDevException(er, exception);
        return errorResponse(er);
    }

    private String exceptionMessage(String message, HttpServletRequest request, HandlerMethod method) {
        return String.format(message + ", Request: {URI=%s, method=%s}",
                request.getRequestURI(),
                Optional.ofNullable(method).map(HandlerMethod::toString).orElse("")
        );
    }

    public ResponseEntity<ExceptionResponse> errorResponse(ExceptionResponse er){
       return ResponseEntity.ok().headers(httpHeaders -> httpHeaders.set(HAS_ERROR, "1")).body(er);
    }
    
    /**
     * 设置异常信息，方便在前端查看异常信息
     *
     * @param er 异常响应对象
     * @param ex 异常
     */
    private void setDevException(ExceptionResponse er, Exception ex) {
        er.setException(ex.getMessage());
        er.setTrace(ex.getStackTrace());
        Throwable cause = ex.getCause();
        if (cause != null) {
            er.setThrowable(cause.getMessage(), cause.getStackTrace());
        }

    }

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messages = new MessageSourceAccessor(messageSource);
    }

    private String translateMessage(String messageCode, Object... args) {
        if (!StringUtils.hasText(messageCode)) {
            messageCode = BaseConstants.ErrorCode.ERROR;
        }
        return this.messages.getMessage(messageCode, args);
    }
}
