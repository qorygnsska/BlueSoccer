package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Goods;
import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import com.example.demo.repository.GoodsRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoxController {
	
	@Autowired
	private GoodsRepository goodsR;
	@Autowired
	private UserRepository userR;
	@Autowired
	private OrdersRepository ordersR;
	
	@GetMapping("/mybox1")
	public String mybox1(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		User user = userR.findByUserId(loggedInUserId); 
		
		if(loggedInUserId == null) {
			redirectAttributes.addFlashAttribute("loginMessage", "로그인 상태가 아닙니다!"); 
			return "redirect:/login";
		}else {
			List<Orders> orders = ordersR.findByUserAndState(user, "장바구니");
			model.addAttribute("orders",orders);
			return "Box";
		}
		
	}
	
	@GetMapping("/mybox")
	public String mybox(Model model, HttpSession session, RedirectAttributes redirectAttributes,
	                    @RequestParam(defaultValue = "1") int page,
	                    @RequestParam(defaultValue = "10") int size) {
	    String loggedInUserId = (String) session.getAttribute("loggedInUserId");
	    User user = userR.findByUserId(loggedInUserId); 
	    
	    if (loggedInUserId == null) {
	        redirectAttributes.addFlashAttribute("loginMessage", "로그인 상태가 아닙니다!"); 
	        return "redirect:/login";
	    } else {
	        // 사용자의 장바구니 페이지네이션
	        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("oid").descending());
	        Page<Orders> ordersPage = ordersR.findByUserAndState(user, "장바구니", pageable);
	        
	        model.addAttribute("orders", ordersPage.getContent());
	        model.addAttribute("currentPage", ordersPage.getNumber() + 1);
	        model.addAttribute("totalPages", ordersPage.getTotalPages());
	        
	        return "Box";
	    }
	}

	
	@PostMapping("/createbox")
	public String createbox(@RequestParam(name="size", required = false) List<String> size, @RequestParam(name="count", required = false) List<Integer> count,
			HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam int gid) {
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		User user = userR.findByUserId(loggedInUserId);
		
		if(loggedInUserId == null) {
			redirectAttributes.addFlashAttribute("loginMessage", "로그인 상태가 아닙니다!"); 
			return "redirect:/login";
		}else {
			for (int i = 0; i < size.size(); i++) {
				Goods goods= goodsR.findByGid(gid);
				Orders oders = new Orders();
				oders.setCount(count.get(i));
				oders.setGoods(goods);
				oders.setSize(size.get(i));
				oders.setState("장바구니");
				oders.setUser(user);
				oders.setPrice(goods.getPrice()*count.get(i));
				oders.setAdress("");
				oders.setUname(user.getName());
				oders.setPhone(user.getPhone());
				oders.setMsg("");
				oders.setRtoken(0);
				ordersR.save(oders);
			}
			
			return "redirect:/mybox";
		}
	}
	
	@PostMapping("/boxorders")
	public String boxorders(@RequestParam(name="oid", required = false) List<Integer> oid,HttpSession session, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("oids", oid);
        return "redirect:/pay";		
	}
	
	@PostMapping("/deletebox")
	public String deletebox(@RequestParam(name="oid", required = false) List<Integer> oid,HttpSession session, RedirectAttributes redirectAttributes) {
		for (int i = 0; i < oid.size(); i++) {
			Orders orders = ordersR.findByOid1(oid.get(i));
			ordersR.delete(orders);
		}
		
		return"redirect:/mybox";
	}
}