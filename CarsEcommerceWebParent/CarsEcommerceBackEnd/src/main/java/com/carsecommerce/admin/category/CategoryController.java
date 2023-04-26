package com.carsecommerce.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carsecommerce.admin.FileUploadUtil;
import com.carsecommerce.admin.user.UserService;
import com.carsecommerce.common.entity.Category;
import com.carsecommerce.common.entity.User;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listFirstPage(Model model) {
		return listByPage(1, model, null);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("keyword") String keyword) {
		Page<Category> page = service.listByPage(pageNum, keyword);
		List<Category> listCategories = page.getContent();

		long startCount = (pageNum - 1) * CategoryService.CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.CATEGORIES_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleURL", "/categories");

		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {

		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Krijo kategori te re");

		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category savedCategory = service.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			service.save(category);
		}

		ra.addFlashAttribute("message", "Kategoria është ruajtur/ndryshuar me sukses.");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Category category = service.get(id);

			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Ndrysho kategorin (ID: " + id + ")");

			return "categories/category_form";			
		} catch (CategoryNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);

			redirectAttributes.addFlashAttribute("message", 
					"Kategoria me ID " + id + " është fshirë me sukses");
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/categories";
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "Aktiv" : "Deaktiv";
		String message = "Kategoria me ID " + id + " eshte kthyer ne statusin " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/categories";
	}
}