package util;

import ModelClass.HomeCate;
import network.Response.ResponseGetAllSubOfSubCategories;

import java.util.ArrayList;

public class GetCategories {

    public static ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> allSubOfSubCategoriesArrayList = new ArrayList<>();
    public static ArrayList<HomeCate> allSubCategoriesArrayList = new ArrayList<>();

    public static ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> subOfSubCategoriesList = new ArrayList<>();

    public static ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> getAllSubOfSubCategoriesArrayList() {
        return allSubOfSubCategoriesArrayList;
    }

    public static void setAllSubOfSubCategoriesArrayList(ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> allSubOfSubCategoriesArrayList) {
        GetCategories.allSubOfSubCategoriesArrayList = allSubOfSubCategoriesArrayList;
    }

    public static ArrayList<HomeCate> getAllSubCategoriesArrayList() {
        return allSubCategoriesArrayList;
    }

    public static void setAllSubCategoriesArrayList(ArrayList<HomeCate> allSubCategoriesArrayList) {
        GetCategories.allSubCategoriesArrayList = allSubCategoriesArrayList;
    }

    public static ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> getSubOfSubCategoriesBySubCategory(String subCategory){
        subOfSubCategoriesList.clear();
        for(int i=0;i<allSubOfSubCategoriesArrayList.size();i++){
            if(allSubOfSubCategoriesArrayList.get(i).getSubOfSubCategoryId().equals(subCategory)){
                ResponseGetAllSubOfSubCategories.AllSubOfSubCategories subOfSubCategories = new ResponseGetAllSubOfSubCategories.AllSubOfSubCategories();
                subOfSubCategories.setCategoryId(allSubOfSubCategoriesArrayList.get(i).getCategoryId());
                subOfSubCategories.setSubOfSubCategoryId(allSubOfSubCategoriesArrayList.get(i).getSubOfSubCategoryId());
                subOfSubCategories.setItem_subCatID(allSubOfSubCategoriesArrayList.get(i).getItem_subCatID());
                subOfSubCategories.setItem_subCatName(allSubOfSubCategoriesArrayList.get(i).getItem_subCatName());
                subOfSubCategories.setImage(allSubOfSubCategoriesArrayList.get(i).getImage());
                subOfSubCategoriesList.add(subOfSubCategories);
            }
        }
        return subOfSubCategoriesList;
    }
}
