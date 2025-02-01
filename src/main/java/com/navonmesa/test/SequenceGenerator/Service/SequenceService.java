package com.realnet.SequenceGenerator.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.SequenceGenerator.Entity.Sequence;
import com.realnet.SequenceGenerator.repo.SeqRepo;

@Service
public class SequenceService {

//	
//	@Autowired
//	private SeqRepo seqRepo;
//	
//	public String GenerateSequence(String name, Long id) {
//		
//		String prefix ="PR";
//		int i =1;
//		String format = "";
//		
//		Sequence s = seqRepo.findById(id).get();
//
//		int starting_no = s.getStating_no();
//		String date_format = s.getDate_format();
//		String digit = s.getSequence_size();
//		String seperator = s.getSeperator();
//		int current_no = s.getCurrent_no();
//		
//		if (name.equalsIgnoreCase("in_prefix")) {
//			prefix=s.getIn_prefix();
//		}
//		if (name.equalsIgnoreCase("pr_prefix")) {
//			prefix=s.getPr_prefix();
//		}
//		if (name.equalsIgnoreCase("cr_prefix")) {
//			prefix=s.getCr_prefix();
//		}
//		
//		Date date = new Date();
//
////		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
//		DateFormat dateFormat = new SimpleDateFormat(date_format);
//
//		String format2 = dateFormat.format(date);
//		
////		final DecimalFormat decimalFormat = new DecimalFormat("0000");
//		final DecimalFormat decimalFormat = new DecimalFormat(digit);
//
//		if (current_no != 0) {
//		 format = decimalFormat.format(current_no);
//
//		}else {
//		format = decimalFormat.format(starting_no);
//
//		}
//		
//		String str = prefix + seperator + format2 + seperator + format;
//
//		s.setCurrent_no(starting_no+1);
//		s.setDemonstration(str);
//		seqRepo.save(s);
//		return str;
//	}
	

	@Autowired
	private SeqRepo seqRepo;

	public Sequence createSequence(Sequence seq){
		return seqRepo.save(seq);
	}
	
	
	public Sequence Savedata(Sequence data) {
		return seqRepo.save(data);
	}
	
	public String GenerateSequence(String pre) {
		return GenerateSequence(pre, String.valueOf(java.time.Year.now()));
	}

	public String GenerateSequence(String pre, String suf) {
		Sequence seq = seqRepo.findByPrefixAndSuffix(pre,suf).orElse(null);

		if(seq == null){
			return "Given Prefix: " + pre + " and Suffix: "+ suf +
					"\nDoes not exist try creating the sequence first....";
		}

		String prefix = seq.getPrefix();
		Integer seq_size = seq.getSequence_size();

		//Current Number Building Logic
		if(seq.getCurrent_no() == null) {
			seq.setCurrent_no(seq.getStarting_no());
		}
		String current_no = String.valueOf( seq.getCurrent_no()+1 );
		if(seq_size == null) seq_size = 5;
		while(current_no.length() < seq_size){
			current_no = "0" + current_no;
		}

		String suffix = seq.getSuffix();
		String sep = seq.getSeperator();

		//--- final Sequence ---
		String new_seq = prefix + sep + current_no + sep + suffix;
		seq.setDemonstration(new_seq);

		// Saving Current-Sequence-State into DataBase
		seq.setCurrent_no(seq.getCurrent_no()+1);
		seqRepo.save(seq);
		return new_seq;
	}
	
	
}
