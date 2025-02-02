package suanfa.C45;

//<span style="font-size:14px;">
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.HashSet;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
  
public class DataSetUtil {  //数据集处理的相关操作
  
      
    /** 
     * 获取数据集中的结果列 
     * @param dataset 
     * @return 
     */  
    public static List<String> getTarget(List<ArrayList<String>> dataset) {  
        List<String> target = new ArrayList<String>();  
        int targetId = dataset.get(0).size() - 1;  
          
        for(List<String> element : dataset) {  
            target.add(element.get(targetId));  
        }  
          
        return target;  
    }  
      
    /** 
     * 获取属性值 
     * @param attrId 
     * @param dataset 
     * @return 
     */  
    public static List<String> getAttributeValue(int attrId, List<ArrayList<String>> dataset) {  
        List<String> attrValue = new ArrayList<String>();  
          
        for(List<String> element : dataset) {  
            attrValue.add(element.get(attrId));  
        }  
          
        return attrValue;  
    }  
  
    /** 
     * 获取属性值，唯一值 
     * @param bestAttr 
     * @param dataset 
     * @return 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public static List<String> getAttributeValueOfUnique(int attrId, List<ArrayList<String>> dataset) {  
        Set attrSet = new HashSet();  
        List<String> attrValue = new ArrayList<String>();  
        for(List<String> element : dataset) {  
            attrSet.add(element.get(attrId));  
        }  
          
        Iterator iterator = attrSet.iterator();  
        while(iterator.hasNext()) {  
            attrValue.add((String) iterator.next());  
        }  
          
        return attrValue;  
    }  
      
    /** 
     * for test <br/> 
     * 输出数据集 
     * @param attribute 
     * @param dataset 
     */  
    public static void printDataset(List<String> attribute, List<ArrayList<String>> dataset) {  
        System.out.println(attribute);  
        for(List<String> element : dataset) {  
            System.out.println(element);  
        }  
    }  
      
    /** 
     * 数据集纯度检测 
     */  
    public static boolean isPure(List<String> data) {  
        String result = data.get(0);
        for(int i = 1; i < data.size(); i++) {  
            if(!data.get(i).equals(result))   
                return false;  
        }  
        return true;  
    }  
  
    /** 
     * 对一列进行概率统计 
     * @param list 
     * @return 
     */  
    public static Map<String, Double> getProbability(List<String> list) {  
        double unitProb = 1.00/list.size();  
        Map<String, Double> probability = new HashMap<String, Double>();  
        for(String key : list) {  
            if(probability.containsKey(key)) {  
                probability.put(key, unitProb + probability.get(key));  
            }else{  
                probability.put(key, unitProb);  
            }  
        }  
        return probability;  
    }  
  
    /** 
     * 根据属性值，分离出结果列target 
     * @param attrValue 
     * @param attrValueList 
     * @param targetList 
     * @return 
     */  
    public static List<String> getTargetByAttribute(String attrValue,   
                            List<String> attrValueList, List<String> targetList) {  
        List<String> result = new ArrayList<String>();  
        for(int i=0; i<attrValueList.size(); i++) {  
            if(attrValueList.get(i).equals(attrValue))   
                result.add(targetList.get(i));  
        }  
        return result;  
    }  
  
    /** 
     * 拿出指定属性值对应的子数据集 
     * @param dataset 
     * @param bestAttr 
     * @param attrValue 
     * @return 
     */  
    public static List<ArrayList<String>> getSubDataSetByAttribute(  
            List<ArrayList<String>> dataset, int attrId, String attrValue) {  
        List<ArrayList<String>> subDataset = new ArrayList<ArrayList<String>>();  
        for(ArrayList<String> list : dataset) {  
            if(list.get(attrId).equals(attrValue)) {  
                ArrayList<String> cutList = new ArrayList<String>();  
                cutList.addAll(list);  
                cutList.remove(attrId);  
                subDataset.add(cutList);  
            }  
        }  
        System.out.println("属性值集："+subDataset);  
          
        return subDataset;  
    }  
  
      
}//</span>  