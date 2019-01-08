package com.rmgs.app.dto;

import lombok.Data;

@Data
public class Paging {

	private Integer pageIndex;
	private Integer pageSize;
	private Integer total;

}
