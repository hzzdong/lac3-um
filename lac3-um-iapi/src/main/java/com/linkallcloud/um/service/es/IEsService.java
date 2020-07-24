package com.linkallcloud.um.service.es;

import java.util.List;

import com.google.common.collect.Multimap;

public interface IEsService {

    boolean instertBulk(Multimap<String, String> ds);

    List<String> getExistIndices(String[] indices);

    boolean deleteIndex(String index);

    String find(String url, String queryStr);

    String cat(String index);

}
