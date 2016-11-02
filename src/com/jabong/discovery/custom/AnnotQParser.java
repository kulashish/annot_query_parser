package com.jabong.discovery.custom;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.InvalidOffsetException;
import gate.util.persistence.PersistenceManager;

public class AnnotQParser extends QParser {

	public AnnotQParser(String queryStr, SolrParams localParams, SolrParams params, SolrQueryRequest request) {
		super(queryStr, localParams, params, request);
	}

	@Override
	public Query parse() throws SyntaxError {
		File appGapp = new File("/Users/ashish/Documents/workspace/other/gate/query_reso_v1.gapp");
		CorpusController controller = null;
		Query query = null;
		try {
			controller = (CorpusController) PersistenceManager.loadObjectFromFile(appGapp);
			Corpus corpus = Factory.newCorpus("QueryLogCorpus");
			FeatureMap params = Factory.newFeatureMap();
			params.put("stringContent", qstr);
			params.put("preserveOriginalContent", new Boolean(true));
			params.put("collectRepositioningInfo", new Boolean(true));
			Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
			corpus.add(doc);
			controller.setCorpus(corpus);
			controller.execute();
			Iterator<Document> iter = corpus.iterator();
			Document annotDoc = iter.next();

			AnnotationSet tokens = annotDoc.getAnnotations("TOK");
			AnnotationSet annotations = annotDoc.getAnnotations("Fashion");
			// StringBuilder outQ = new StringBuilder();
			BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
			// for (Annotation token : tokens.inDocumentOrder()) {
			// if (!token.getType().equalsIgnoreCase("Token"))
			// continue;
			// long start = token.getStartNode().getOffset();
			// long end = token.getEndNode().getOffset();
			// AnnotationSet fashionAnnotations =
			// annotations.getContained(start, end);
			// String tokenString = annotDoc.getContent().getContent(start,
			// end).toString();
			// if (fashionAnnotations.isEmpty())
			// booleanQueryBuilder.add(new TermQuery(new Term("fulltext",
			// tokenString)), Occur.SHOULD);
			// else {
			// for (Annotation a : fashionAnnotations.inDocumentOrder())
			// booleanQueryBuilder.add(new TermQuery(new Term(a.getType(),
			// tokenString)), Occur.SHOULD);
			// }
			// }
			long lastEnd = 0;
			for (Annotation fashionAnnot : annotations.inDocumentOrder()) {
				long start = fashionAnnot.getStartNode().getOffset();
				long end = fashionAnnot.getEndNode().getOffset();

				for (Annotation a : tokens.getContained(lastEnd, start).inDocumentOrder()) {
					if (!a.getType().equalsIgnoreCase("Token"))
						continue;
					booleanQueryBuilder.add(new TermQuery(new Term("fulltext", getTokenString(annotDoc, a))),
							Occur.SHOULD);
				}

				booleanQueryBuilder.add(
						new TermQuery(new Term(fashionAnnot.getType(), getTokenString(annotDoc, fashionAnnot))),
						Occur.SHOULD);
				lastEnd = end;
			}
			query = booleanQueryBuilder.build();
			System.out.println(query.toString());
			// String xmlDoc =
			// annotDoc.toXml(annotDoc.getAnnotations("Fashion"), false);
			// System.out.println(xmlDoc);
		} catch (PersistenceException | ResourceInstantiationException | IOException | ExecutionException
				| InvalidOffsetException e) {
			e.printStackTrace();
		}

		return query;
	}

	private String getTokenString(Document annotDoc, Annotation a) throws InvalidOffsetException {
		long start = a.getStartNode().getOffset();
		long end = a.getEndNode().getOffset();
		return annotDoc.getContent().getContent(start, end).toString();
	}

	public static void main(String[] args) {
		AnnotQParserPlugin parserPlugin = new AnnotQParserPlugin();
		parserPlugin.init(null);
		try {
			QParser parser = parserPlugin.createParser("barcelona t shirts for boys less than 500 rupees", null, null,
					null);
			parser.parse();
		} catch (SyntaxError e) {
			e.printStackTrace();
		}
	}

}
