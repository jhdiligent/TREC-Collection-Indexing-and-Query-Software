package indexer;

import java.io.File;

public class DocProcessor {
	
	private String directory;
	private String fileList[];
	
	public DocProcessor(String directory){
		if(!isDirectory(directory))return;
		this.directory=directory;
		File dirFile=new File(directory);
		this.fileList=dirFile.list();
	}
	
	public boolean isDirectory(String directory) {
		File dirFile=new File(directory);
		if(!dirFile.exists()) {
			System.out.println("File path doesn't exist");
		    return false;
		}
		if(!dirFile.isDirectory()) {
			System.out.println("File path isn't point to a directory");
			return false;
		}
		return true;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public String[] getFileList() {
		return fileList;
	}
}
