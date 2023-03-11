package sethe.tripbuilder.transform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSV2SQL {
	private static final String delimit = ",";
	private static String schema = "tripbuilder";
	private static final String[] key_words= {"from", "to", "table", "column", "date"};
	private static int count = 500; 

	public static void main(String[] args) throws IOException {
		String source = args[0];

		csv2sql(source);
	}

	private static void csv2sql(String source) throws IOException {
		File fin = new File(source);
		String sout = fin.getAbsolutePath().replace(".csv", ".sql");
		File fout = new File(sout);
		String tableName = schema + "." + fin.getName().replace(".csv", "");

		BufferedReader br = new BufferedReader(new FileReader(fin));
		BufferedWriter bw = new BufferedWriter(new FileWriter(fout));

		try {
			String header = br.readLine();
			String[] columns = header.split(delimit);

			String createTable = "CREATE TABLE " + tableName + "(\n";
			for(String col : columns) {
				col = col.replace("\"", "").trim();
				col = checkKeyWord(col);
				createTable += col + " varchar(100),\n";
			}
			createTable = createTable.substring(0, createTable.lastIndexOf(","));
			createTable += ");\n";

			bw.write(createTable);
			bw.flush();

			String line = br.readLine();
			int count = 0;
			while(line != null) {
				if(count > 500) {
					bw.flush();
					count = 0;
				}

				String insert = createInsert(line, tableName);
				bw.write(insert);
				line = br.readLine();
				count++;
			}
			bw.flush();
		} finally {
			br.close();
			bw.close();
		}
	}

	private static String createInsert(String line, String tableName) {
		String insert = "INSERT INTO " + tableName + " VALUES (";

		String vals[] = line.split(",");
		for(String val : vals) {
			val = val.replace("'", "''");
			val = val.trim();
			if(val.charAt(0) == '"') {
				val = val.substring(1);
			}
			if(val.charAt(val.length()-1) == '"') {
				val = val.substring(0, val.length()-1);
			}

			val = val.trim();

			insert += "'" + val + "', ";
		}
		
		insert = insert.substring(0, insert.lastIndexOf(","));

		insert += ");\n";
		count --;

		if(count < 0) {
			count = 500;
			insert += "COMMIT;\n";
		}

		return insert;
	}

	private static String checkKeyWord(String val) {
		for(String key : key_words) {
			if(key.equalsIgnoreCase(val)) {
				val = val+"1";
				break;
			}
		}
		return val;
	}
}