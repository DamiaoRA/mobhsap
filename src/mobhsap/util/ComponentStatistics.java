package mobhsap.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ComponentStatistics {
	private static final String FILE_OUT = "/home/damiao/relatorio.csv";
	private static final String HEAD = "input;traj2text;etl;datamanager;dwManager";
	
	private static ComponentStatistics cs;
	private BufferedWriter bw;
	
	private Long inputTime = 0l;
	private Long traj2TextTime = 0l;
	private Long dataManagerTime = 0l;
	private Long etlTime = 0l;
	private Long dwManagerTime = 0l;

	private ComponentStatistics() throws IOException {
		bw = new BufferedWriter(new FileWriter(FILE_OUT));
		bw.write(HEAD);
		bw.newLine();
	}

	public void flush() throws IOException {
		bw.write(inputTime + ";" + traj2TextTime + ";" + dataManagerTime + ";" + etlTime + ";" + dwManagerTime);
		bw.newLine();
	}

	public void close() throws IOException {
		bw.close();
	}

	public static ComponentStatistics getInstance() throws IOException {
		if(cs == null) {
			cs = new ComponentStatistics();
		}
		return cs;
	}

	public void setBw(BufferedWriter bw) {
		this.bw = bw;
	}

	public void setInputTime(Long inputTime) {
		this.inputTime = inputTime;
	}

	public void setTraj2TextTime(Long traj2TextTime) {
		this.traj2TextTime = traj2TextTime;
	}

	public void setDataManagerTime(Long dataManagerTime) {
		this.dataManagerTime = dataManagerTime;
	}

	public void setEtlTime(Long etlTime) {
		this.etlTime = etlTime;
	}

	public void setDwManagerTime(Long dwManager) {
		this.dwManagerTime = dwManager;
	}

	public static void main(String[] args) throws IOException {
		ComponentStatistics cs = ComponentStatistics.getInstance();
		
		cs.setInputTime(1L);
		cs.setTraj2TextTime(2L);
		cs.setDataManagerTime(3L);
		cs.setEtlTime(4L);
		cs.setDwManagerTime(5L);
		cs.flush();
		
		cs.setInputTime(10L);
		cs.setTraj2TextTime(20L);
		cs.setDataManagerTime(30L);
		cs.setEtlTime(40L);
		cs.setDwManagerTime(50L);
		cs.flush();
		
		cs.close();
	}
}
