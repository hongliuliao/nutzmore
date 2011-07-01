/**
 * 
 */
package org.nutz.dao.cache;

/**
 * @author liaohongliu
 *
 * 创建时间: 2011-4-22
 */
public class CacheException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CacheException(Exception e) {
		super(e);
	}
}
