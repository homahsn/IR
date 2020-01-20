package lucene_project;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.wordnet.SynExpand;
import org.apache.lucene.wordnet.Syns2Index;

public class QueryExapntion
{
	private FSDirectory dir;
	private IndexSearcher searcher;
	private Analyzer analyzer;
	public QueryExapntion(String wordnext_index_path) throws Exception
	{
		dir = FSDirectory.open(new File(wordnext_index_path));
		searcher = new IndexSearcher(dir,true);
		analyzer = new StandardAnalyzer(Version.LUCENE_31);
	}
	public String expandQuery(String query) throws Exception
	{
		Query q = SynExpand.expand(query, searcher,analyzer, "body", 0.2f);
		return q.toString();
	}
	public static void indexWordnet(String wordnet_file,String output_index_dir) throws Throwable
	{
		String[] syns_args = new String[2];
		syns_args[0] = wordnet_file;
		syns_args[1] = output_index_dir;
		Syns2Index.main(syns_args);
	}
	
}
