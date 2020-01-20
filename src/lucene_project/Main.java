package lucene_project;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;



public class Main
{
	//private static final String dataPath ="C:\\Users\\Homa\\Desktop\\lucene\\data\\data"; 
	private static final String dataPath ="C:\\Users\\Homa\\Desktop\\lucene\\DataSet\\DataSet";
	
	public static void main(String args[])
	{
		try
		{
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
			//WhitespaceAnalyzer analyzer  = new WhitespaceAnalyzer();
			Directory index = new RAMDirectory();//FSDirectory.open(Paths.get("./lucene_indexes/"));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_31,analyzer);
			IndexWriter w = new IndexWriter(index, config);
			addDocs(w,dataPath);
			System.out.println("indexing done!");
			
			//interactive mode
			/*QueryParser qpar = new QueryParser(Version.LUCENE_31,"body",analyzer);
			Scanner ss = new Scanner(System.in);
			while(true)
			{
				System.out.println("Enter your query:");
				Query q = qpar.parse(ss.nextLine());
				
				
				int hitsPerPage = 10;
				IndexSearcher searcher = new IndexSearcher(index);
				TopDocs docs = searcher.search(q, hitsPerPage);
				ScoreDoc[] hits = docs.scoreDocs;
				
				System.out.println("Found " + hits.length + " hits.");
				for(int i=0;i<hits.length;++i) {
				    int docId = hits[i].doc;
				    Document d = searcher.doc(docId);
				    System.out.println((i + 1) + ". " + d.get("file"));
				}
				
			}*/
			
			
			
			
			
		
			//batch test mode
			
			QueryExapntion qe = new QueryExapntion("./wordnet_index/");
			
			
			QueryParser qpar = new QueryParser(Version.LUCENE_31,"body",analyzer);
			File folder = new File(dataPath);
			File[] input_files = folder.listFiles();
			int total = 0;
			int correct = 0;
			for(File file:input_files)
			{
				if(!file.isFile())//not a directory
					continue;
				SOQuestion soq = new SOQuestion(file);
				String title = soq.getTitle();
				
				String expanded_query = qe.expandQuery(QueryParser.escape(title));
				Query q = qpar.parse(expanded_query);
				
				IndexSearcher searcher = new IndexSearcher(index);
				TopDocs docs = searcher.search(q,10);
				ScoreDoc[] hits = docs.scoreDocs;
				
				for(int i=0;i<hits.length;++i)
				{
				    int docId = hits[i].doc;
				    Document d = searcher.doc(docId);
				    String hit_filename = d.get("file");
				    String soq_filename = soq.getFileName();
				    if(hit_filename.equals(soq_filename))
				    {
				    	correct++;
				    	break;
				    }
				}
				total++;
				System.out.println("Total="+total+", correct="+correct);
			}
			
		}
		catch (Exception e)
		{
		
			e.printStackTrace();
		}
	}
	private static void addDocs(IndexWriter w,String path) throws Exception
	{
		File folder = new File(path);
		File[] input_files = folder.listFiles();
		int ind = 1;
		int all = input_files.length;
		
		for(File file:input_files)
		{
			if(!file.isFile())//not a directory
				continue;
			addDoc(w, file);
			System.out.println("Indexing "+ind+"/"+all);
			ind++;
		}
		w.close();
	}
	private static void addDoc(IndexWriter w,File f) throws Exception
	{	
		SOQuestion q = new SOQuestion(f);
		Document doc = new Document();
		
		doc.add(new Field("body",q.getQuestionAndAnswers(),Field.Store.YES,Index.ANALYZED));
		doc.add(new Field("file",q.getFileName(),Field.Store.YES,Index.ANALYZED));
		w.addDocument(doc);
	}
	
}
