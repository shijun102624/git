import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * 工具类(静态方法,可以通过类名直接调用)
 */
public class luceneUtil {
    private static IndexWriterConfig config;
    private static Version version;
    private static Analyzer analyzer;
    private static Directory directory;
    static {
        try {
            version = Version.LUCENE_44;
            analyzer = new StandardAnalyzer(Version.LUCENE_44);
            config = new IndexWriterConfig(version,analyzer);
            directory = FSDirectory.open(new File("e:/index"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //IndexWriter
    public static  IndexWriter getIndexWriter(){
        //只需要打开一次,写在静态代码块中
        IndexWriter indexWriter=null;
        try {
            indexWriter = new IndexWriter(directory,config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }
    //IndexSearcher
    public static IndexSearcher getIndexReader(){
        //索引搜索器,读入流
        IndexReader indexReader = null;
        IndexSearcher indexSearcher=null;
        try {
            indexReader = DirectoryReader.open(directory);
            indexSearcher = new IndexSearcher(indexReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexSearcher;
    }
    //commit   close
    public static void commit(IndexWriter indexWriter){
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //roolback
    public static void roolback(IndexWriter indexWriter){
        try {
            indexWriter.rollback();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
