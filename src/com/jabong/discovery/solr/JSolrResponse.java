package com.jabong.discovery.solr;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class JSolrResponse {

	private QueryResponse solrResponse;
	private SolrDocumentList resultDocs;

	public JSolrResponse(QueryResponse response) {
		solrResponse = response;
		resultDocs = solrResponse.getResults();
	}

	public long numResults() {
		return resultDocs.getNumFound();
	}

}
