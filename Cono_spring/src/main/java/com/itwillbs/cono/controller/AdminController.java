package com.itwillbs.cono.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.cono.service.AdminService;
import com.itwillbs.cono.vo.AdminNoticeDTO;
import com.itwillbs.cono.vo.PageInfo;

@Controller
public class AdminController {

	@Autowired
	AdminService service;

	// -------------- 관리자 메인 이동 (관리자) - 김도은 -------------
	@RequestMapping(value = "admin_center_main", method = RequestMethod.GET)
	public String adminCenter() {
		return "/admin_center/main";
	}
	// -------------- 고객센터 공지사항 리스트, 검색기능 (관리자) - 김도은 -------------

	// => 파라미터로 전달되는 pageNum 파라미터가 없을 경우를 대비하여 기본값 0 으로 지정
	@RequestMapping(value = "AdminNoticeList", method = RequestMethod.GET)
	public String list(@RequestParam(defaultValue = "") String searchType,
			@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "1") int pageNum,
			Model model) {

		int listCount = service.getNoticeListCount("searchType", "search");
		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수
		int pageLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수

		// 페이징 처리를 위한 계산 작업
		int maxPage = (int) Math.ceil((double) listCount / listLimit);
		int startPage = ((int) ((double) pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}

		int startRow = (pageNum - 1) * listLimit;

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNum(pageNum);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		pageInfo.setListCount(listCount);
		pageInfo.setStartRow(startRow);
		pageInfo.setListLimit(listLimit);

		List<AdminNoticeDTO> noticeList = service.getNoticeList(searchType, "%" + search + "%", pageInfo);

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("pageInfo", pageInfo);

		return "userCenter/admin_notice_list";
	}

	// 고객센터 공지사항 리스트 - POST
	@RequestMapping(value = "AdminNoticeList", method = RequestMethod.POST)
	public String noticeList(@RequestParam String searchType, @RequestParam String search,
			@RequestParam(defaultValue = "1") int pageNum, Model model) {

		int listCount = service.getNoticeListCount(searchType, "%" + search + "%");

		int listLimit = 10; // 한 페이지 당 표시할 게시물 목록 갯수
		int pageLimit = 10; // 한 페이지 당 표시할 페이지 목록 갯수

		int maxPage = (int) Math.ceil((double) listCount / listLimit);
		int startPage = ((int) ((double) pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}

		int startRow = (pageNum - 1) * listLimit;

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNum(pageNum);
		pageInfo.setMaxPage(maxPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		pageInfo.setListCount(listCount);
		pageInfo.setStartRow(startRow);
		pageInfo.setListLimit(listLimit);
		List<AdminNoticeDTO> noticeList = service.getNoticeList(searchType, "%" + search + "%", pageInfo);

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("searchType", searchType);
		model.addAttribute("search", search);

		return "userCenter/admin_notice_list";
	}
}
