package com.jabong.discovery.custom;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;

import gate.CorpusController;
import gate.Gate;
import gate.util.GateException;

public class AnnotQParserPlugin extends QParserPlugin {

	private CorpusController controller;

	@Override
	public void init(NamedList args) {
		try {
			Gate.init();
		} catch (GateException e) {
			System.out.println("FATAL: GATE failed to initialize!");
		}
	}

	@Override
	public QParser createParser(String queryStr, SolrParams localParams, SolrParams params, SolrQueryRequest request) {
		return new AnnotQParser(queryStr, localParams, params, request);
	}

}
