public static List<SelectorOptionBean> getJournalArticles(long groupId, HttpServletRequest request) {
		List<SelectorOptionBean> listOptions = new ArrayList<SelectorOptionBean>();
		try {
			//Condiciones de busqueda
			String[] entryClassNames = {JournalArticle.class.getName()};
			SearchContext searchContext = SearchContextFactory.getInstance(request);
			searchContext.setAttribute("paginationType", "none");
			searchContext.setStart(QueryUtil.ALL_POS);
			searchContext.setEnd(QueryUtil.ALL_POS);
			
			//Queries
			BooleanQuery fullQuery = BooleanQueryFactoryUtil.create(searchContext);
			BooleanQuery searchQuery = BooleanQueryFactoryUtil.create(searchContext);
			searchQuery.addRequiredTerm("head", Boolean.TRUE);
			searchQuery.addRequiredTerm("groupId", groupId);
			fullQuery.add(searchQuery, BooleanClauseOccur.MUST);
			searchContext.setEntryClassNames(entryClassNames);
			
			Hits hits = SearchEngineUtil.search(searchContext, fullQuery);
			
			for(com.liferay.portal.kernel.search.Document doc: hits.toList()){
				String articleId = doc.get("articleId");
				String title = doc.get(LOCALE, "title");
				SelectorOptionBean groupOptionBean = new SelectorOptionBean(articleId, title + HogarConst.ABRE_PARENTESIS + articleId + HogarConst.CIERRA_PARENTESIS);
				listOptions.add(groupOptionBean);
			}
			Collections.sort(listOptions);
		} catch (SearchException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		listOptions.add(0, new SelectorOptionBean(HogarConst.VACIO,"- - Contenido - -"));
		return listOptions;
	}

	public static List<SelectorOptionBean> getDocuments(long groupId, HttpServletRequest request) {
		List<SelectorOptionBean> listOptions = new ArrayList<SelectorOptionBean>();
		try {
			//Condiciones de busqueda			
			SearchContext searchContext = SearchContextFactory.getInstance(request);
			searchContext.setAttribute("paginationType", "none");
			searchContext.setStart(QueryUtil.ALL_POS);
			searchContext.setEnd(QueryUtil.ALL_POS);			
			
			Hits hits = DLAppServiceUtil.search(groupId, searchContext);
			for(com.liferay.portal.kernel.search.Document doc: hits.toList()){
				if(!doc.get("mimeType").contains("image")){
					String fileEntryId= doc.get(Field.ENTRY_CLASS_PK);
					String title= doc.get(LOCALE, "title");
					SelectorOptionBean groupOptionBean = new SelectorOptionBean(fileEntryId, title + HogarConst.ABRE_PARENTESIS + fileEntryId + HogarConst.CIERRA_PARENTESIS);
					listOptions.add(groupOptionBean);
				}
			}
			Collections.sort(listOptions);
		} catch (SearchException e) {
			e.printStackTrace();
		}
		return listOptions;
	}