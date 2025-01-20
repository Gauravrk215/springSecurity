package com.realnet.rb.service;

import java.util.List;

import com.realnet.rb.entity.Rn_rb_Column;
import com.realnet.rb.response.Rn_columnDTO;

public interface Rn_rb_column_service {

	List<Rn_rb_Column> save(List<Rn_rb_Column> rn_tables);
	List<Rn_columnDTO> getListOfColumns(int id);
}
