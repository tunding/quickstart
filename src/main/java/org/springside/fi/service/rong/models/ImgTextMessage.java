package org.springside.fi.service.rong.models;

import org.springside.modules.mapper.JsonMapper;


//图文消息
public class ImgTextMessage extends Message {
	private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
	private String content;
	private String title;
	private String imageUri;
	private String extra;

	public ImgTextMessage(String content, String title, String imageUri) {
		this.type = "RC:ImgTextMsg";
		this.content = content;
		this.title = title;
		this.imageUri = imageUri;
	}

	public ImgTextMessage(String content, String title, String imageUri,
			String extra) {
		this(content, title, imageUri);
		this.extra = extra;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return jsonMapper.toJson(this);
	}
}
