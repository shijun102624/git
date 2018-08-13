import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/8/9 0009.
 */

public class test {
    @Test
    public void testCreateIndex(){
        IndexWriter indexWriter =null;
        try {
            //1创建索引写入器
            //参数dir表示索引的文件夹目录  参数2表示索引写入器的相关配置
            indexWriter = luceneUtil.getIndexWriter();
            //创建文档模型
            //八种基本类型和StringField是不分词的，TextField是分词的
            Document document = new Document();
            document.add(new StringField("id","1",Field.Store.YES));
            document.add(new StringField("title","我是中国人",Field.Store.YES));
            document.add(new StringField("author","施军",Field.Store.YES));
            document.add(new TextField("content","我爱祖国",Field.Store.YES));
            indexWriter.addDocument(document);
            luceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
           luceneUtil.roolback(indexWriter);
        }
    }
    @Test
    public void testSearchIndex() throws IOException {
        //索引搜索器,读入流
        IndexSearcher indexSearcher = luceneUtil.getIndexReader();
        //参数1查询关键字    2查询条数
        Query query = new TermQuery(new Term("content","我"));
        TopDocs topDocs = indexSearcher.search(query, 100);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            //分数
          // float score = scoreDoc.score;
            //页码(获得docId)索引区和元数据区
            int doc = scoreDoc.doc;
            //通过docId获得元数据区的document的对象
            Document document = indexSearcher.doc(doc);
            System.out.println("这是文章id  "+document.get("id"));
            System.out.println("这是文章title  "+document.get("title"));
            System.out.println("这是文章author  "+document.get("author"));
            System.out.println("这是文章content  "+document.get("content"));
        }
       // System.out.println("这是文章分数  "+score);
    }

    @Test
    public void testDeleteIndex(){
        IndexWriter indexWriter = luceneUtil.getIndexWriter();
        try {
            //indexWriter.deleteAll();
            indexWriter.deleteDocuments(new Term("id","2"));
            luceneUtil.commit(indexWriter);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            luceneUtil.roolback(indexWriter);
        }
    }

    @Test
    public void testUpdateIndex(){
        IndexWriter indexWriter = luceneUtil.getIndexWriter();
        Document document = new Document();
        document.add(new StringField("id","2",Field.Store.YES));
        document.add(new StringField("title","中国人2",Field.Store.YES));
        document.add(new StringField("author","施军2",Field.Store.YES));
        document.add(new TextField("content","我爱祖国2",Field.Store.YES));
        try {
            indexWriter.updateDocument(new Term("id","2"),document);
            luceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            luceneUtil.roolback(indexWriter);
        }
    }
}
