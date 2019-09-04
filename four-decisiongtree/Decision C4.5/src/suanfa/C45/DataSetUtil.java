package suanfa.C45;

//<span style="font-size:14px;">
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.HashSet;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
  
public class DataSetUtil {  //���ݼ��������ز���
  
      
    /** 
     * ��ȡ���ݼ��еĽ���� 
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
     * ��ȡ����ֵ 
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
     * ��ȡ����ֵ��Ψһֵ 
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
     * ������ݼ� 
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
     * ���ݼ����ȼ�� 
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
     * ��һ�н��и���ͳ�� 
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
     * ��������ֵ������������target 
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
     * �ó�ָ������ֵ��Ӧ�������ݼ� 
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
        System.out.println("����ֵ����"+subDataset);  
          
        return subDataset;  
    }  
  
      
}//</span>  