package query;

import java.io.IOException;

public class query_main {

	public static void main(String args[]) throws IOException {	 
		//read query from file  query_desc.51-100.short.txt stoplist stemclass
		QueryRunner runner=new QueryRunner();
		runner.run(args);
	}
}
