package com.realnet.FabricIcardLines.Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.realnet.FabricIcard.Entity.FabricIcard;
import com.realnet.FabricIcard.Repository.FabricIcardRepository;
import com.realnet.FabricIcardLines.Entity.FabricIcardLines;
import com.realnet.FabricIcardLines.Repository.FabricIcardLinesRepository;

@Service
public class FabricIcardLinesService {

	@Value("${projectPath}")
	private String projectpath;

	@Autowired
	private FabricIcardRepository fabricIcardRepository;

	@Autowired
	private FabricIcardLinesRepository Repository;

	public FabricIcardLines Savedata(FabricIcardLines data) {
		return Repository.save(data);
	}

	public List<FabricIcardLines> getdetails() {
		return (List<FabricIcardLines>) Repository.findAll();
	}

	public FabricIcardLines getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public FabricIcardLines update(FabricIcardLines data, Long id) {
		FabricIcardLines old = Repository.findById(id).get();
		old.setHeader_id(data.getHeader_id());
		old.setModel(data.getModel());
		old.setLayoutModel(data.getLayoutModel());
		old.setFile_path(data.getFile_path());
		old.setXml_file_name(data.getXml_file_name());
		old.setJson_file_name(data.getJson_file_name());
		old.setMapping_model(data.getMapping_model());

		final FabricIcardLines test = Repository.save(old);
		return test;
	}

//	make xml file
	public FabricIcardLines makexmlfile(String headerid, String xml, String json) throws IOException {

		FabricIcard fabricIcard = fabricIcardRepository.findById(Long.valueOf(headerid)).get();

		String name = fabricIcard.getName();
		String xmlfileName = name + ".xml";
		String jsonfileName = name + ".txt";

		String filePath = Paths.get(xmlfileName).toString();
		FileWriter fr = new FileWriter(filePath);
		fr.write(xml);
		fr.close();

		String jsonfilePath = Paths.get(jsonfileName).toString();
		FileWriter jsonfr = new FileWriter(jsonfilePath);
		jsonfr.write(json);
		jsonfr.close();

		FabricIcardLines old = Repository.getbyheaderId(headerid);

		old.setFile_path(projectpath);
		old.setXml_file_name(xmlfileName);
		old.setJson_file_name(jsonfileName);
		old.setHeader_id(headerid);

		final FabricIcardLines test = Repository.save(old);
		return test;
	}

//	get by header id
	public FabricIcardLines getbyheaderid(String id) {
		return Repository.getbyheaderId(id);
	}

//	read file
	public String readjson(String id) throws IOException {

		FabricIcardLines fLines = Repository.getbyheaderId(id);
		String file_path = fLines.getFile_path();
		String json_file_name = fLines.getJson_file_name();
		String pathString = file_path + File.separator + json_file_name;

		String readFileToString = FileUtils.readFileToString(new File(pathString), StandardCharsets.UTF_8);

		return readFileToString;
	}
}