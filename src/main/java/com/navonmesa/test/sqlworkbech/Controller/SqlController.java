package com.realnet.sqlworkbech.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.sqlworkbech.Entity.SqlModel;
import com.realnet.sqlworkbech.Repository.SqlRepository;

@RequestMapping("/SqlworkbenchSqlcont")
@RestController
public class SqlController {

	@Autowired
	private SqlRepository sqlRepository;

//	CREATE DATA 
	@PostMapping("/sql")
	public SqlModel create(@RequestBody SqlModel sqlModel) {
		SqlModel sql = sqlRepository.save(sqlModel);
		return sql;
	}

//	UPDATE BY ID 
	@PutMapping("/sql/{id}")
	public SqlModel update(@PathVariable Long id, @RequestBody SqlModel sqlModel) {
		SqlModel newsq = sqlRepository.findById(id).orElseThrow(null);
		newsq.setConn_string(sqlModel.getConn_string());
		newsq.setDriver_class_name(sqlModel.getDriver_class_name());
		newsq.setUsername(sqlModel.getUsername());
		newsq.setPassword(sqlModel.getPassword());
		newsq.setName(sqlModel.getName());
		newsq.setType(sqlModel.getType());
		newsq.setDescription(sqlModel.getDescription());
		newsq.setActive(sqlModel.isActive());

		SqlModel sql = sqlRepository.save(newsq);
		return sql;
	}

//	GET BY ID 
	@GetMapping("/sql/{id}")
	public SqlModel getbyid(@PathVariable Long id) {
		SqlModel newsq = sqlRepository.findById(id).orElseThrow(null);
		return newsq;
	}

//	GET ALL 
	@PreAuthorize("hasAnyRole('ADMIN','ProjectManager','SYSADMIN')")
	@GetMapping("/sql")
	public ResponseEntity<?> getall() {
		List<SqlModel> sql = sqlRepository.findAll();
		return new ResponseEntity<>(sql, HttpStatus.OK);
	}

//	DELETE BY ID 
	@DeleteMapping("/sql/{id}")
	public void delete(@PathVariable Long id) {
		SqlModel newsq = sqlRepository.findById(id).orElseThrow(null);
		sqlRepository.delete(newsq);

	}
}
