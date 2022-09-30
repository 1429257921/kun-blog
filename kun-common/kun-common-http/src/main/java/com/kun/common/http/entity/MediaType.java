package com.kun.common.http.entity;

import lombok.Getter;

/**
 * 媒体类型
 *
 * @author gzc
 * @since 2022-6-8 9:44
 **/
@Getter
public enum MediaType {
	/**
	 * 媒体类型
	 */
	PNG_MEDIA_TYPE("image/png; charset=utf-8"),
	PDF_MEDIA_TYPE("application/pdf; charset=utf-8"),
	;


	private String value;

	MediaType(String value) {
		this.value = value;
	}
}
