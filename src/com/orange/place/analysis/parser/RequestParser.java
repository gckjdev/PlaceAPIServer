package com.orange.place.analysis.parser;

import com.orange.place.analysis.domain.ParseResult;
import com.orange.place.analysis.domain.Request;

public interface RequestParser {

	ParseResult parse(Request request);
}
