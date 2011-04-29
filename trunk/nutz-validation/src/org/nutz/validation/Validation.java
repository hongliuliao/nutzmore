package org.nutz.validation;

import org.nutz.validation.annotation.AnnotationValidation;

/**
 * 验证工具类的接口。
 * 
 * @see AnnotationValidation
 * @author QinerG(QinerG@gmail.com)
 */
public interface Validation {

	public Errors validate(Object target);

	public void validate(Object target, Errors errors);

}
