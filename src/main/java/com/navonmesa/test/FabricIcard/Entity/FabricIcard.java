package com.realnet.FabricIcard.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.FabricIcardLines.Entity.FabricIcardLines;

import lombok.Data;

@Entity
@Data
public class FabricIcard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private boolean active;
	private String url;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "layoutBuilder_t")
	private List<FabricIcardLines> fabricIcardLines = new ArrayList<>();

}