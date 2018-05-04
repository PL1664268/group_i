import java.io.*;

import java.util.*;





class Person implements Serializable{

	private static final long serialVersionUID = 1L;

	String name1;

	String name2;

	int year;

	int month;

	int day;

	

	

	

	Person(String name1, String name2, int year, int month, int day){

		this.name1 = name1;

		this.name2 = name2;

		this.year = year;

		this.month = month;

		this.day = day;

	}

	

	public String toString() {

	    String name=name1 + "　" + name2;

	    return String.format("%4d年%2d月%2d日 %s",year,month,day,name);

	}

	

	

}



public class J10_5 {



	public static void main(String[] args) {

		// TODO 自動生成されたメソッド・スタブ

		try {

			File f = new File(args[0]);

			FileReader fr = new FileReader(f);

			BufferedReader br = new BufferedReader(fr);

			StreamTokenizer st = new StreamTokenizer(br);

			ArrayList<Person> array = new ArrayList<Person>();

			

			String name1 = null, name2 = null;

			int year = 0, month = 0, day = 0;

			int count1 = 0, count2 = 0;



            st.whitespaceChars('/','/');

            st.whitespaceChars(',',',');

            

            while(st.nextToken() != StreamTokenizer.TT_EOF) {

                switch(st.ttype) {

                     case StreamTokenizer.TT_WORD: 

                    	 if(count1 == 0){

                    		 name1 = st.sval;

                    		 count1++;

                    	 }else if(count1 == 1){

                    		 name2 = st.sval;

                    		 count1++;

                    	 }

                         break;

                     case StreamTokenizer.TT_NUMBER: 

                    	 if(count2 == 0){

                    		 year = (int)st.nval;

                    		 count2++;

                    	 }else if(count2 == 1){

                    		 month = (int)st.nval;

                    		 count2++;

                    	 }else if(count2 == 2){

                    		 day = (int)st.nval;

                    		 count2++;

                    	 }

                         break;

                }

                

                if(count1+count2 == 5){

                	array.add(new Person(name1, name2, year, month, day));

                	count1 = 0;

                	count2 = 0;

                }

            }



            FileOutputStream fos = new FileOutputStream(args[1]);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(Person p : array){

            	oos.writeObject(p);

            }

            oos.close();

            

            FileInputStream fis = new FileInputStream(args[1]);

            ObjectInputStream ois = new ObjectInputStream(fis);

            try{

            	while(true){

            		System.out.println((Person)ois.readObject());

            	}

            } catch (EOFException e) {

            }

           

		} catch (FileNotFoundException e) {

			// TODO 自動生成された catch ブロック

			e.printStackTrace();

		} catch (IOException e) {

			// TODO 自動生成された catch ブロック

			e.printStackTrace();

		} catch (ClassNotFoundException e) {

			// TODO 自動生成された catch ブロック

			e.printStackTrace();

		}

	}



}