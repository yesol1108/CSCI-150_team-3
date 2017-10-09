import java.math.BigInteger ;
import java.util.Random ;
import java.io.* ;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;

public class RSAlog
{
	int size;					
	BigInteger p,q;			
	BigInteger N;
	BigInteger r;
	BigInteger E,D;  
	String nt,dt,et;
	
	String publicKey;
	String privateKey;
	String randomNumber;
	
	BigInteger[] ciphertext;
	int m[] = new int[1000];
	String st[] = new String[1000];
	String str="";
	String s_array1[]= new String[100000];
	
	StringBuffer sb1 = new StringBuffer();
	String inputMessage, encryptedData, decryptedMessage;
	
	public void RSA(int size)
	{
		this.size=size;
		generatePrimeNumbers();  //Generates two prime numbers p,q
		generateKeys();			//Generates Public and Private keys
		
		BigInteger publicKeyX = get_E();
		BigInteger privateKeyX = get_D();
		BigInteger randomNumberX = get_N();
		publicKey= publicKeyX.toString();
		privateKey= privateKeyX.toString();
		randomNumber= randomNumberX.toString();
		System.out.println("Public Key (E,N):"+publicKey+","+randomNumber);
		System.out.println("Private Key (E,N):"+privateKey+","+randomNumber);
		
		/*Scanner scan= new Scanner(System.in);
		
		System.out.print("Enter username: ");*/
		inputMessage="singharbhajanhs";
		encryptedData=RSAencrypt(inputMessage);
		System.out.println("Encrypted as :"+encryptedData);
		
		decryptedMessage=RSAdecrypt();
		System.out.print("Decrypted as :"+decryptedMessage);
	}
	
	public void generatePrimeNumbers()	//Generating Prime Numbers p,q
	{
		Random rand = new Random();
		p= new BigInteger(size,10,new Random());
		do
		{
			q= new BigInteger(size,10,new Random());
		    
		}
		while(q.compareTo(p)==0);
	}
	
	public void generateKeys()
	{
		N=p.multiply(q);
		r=p.subtract(BigInteger.valueOf(1));
		r=r.multiply(q.subtract(BigInteger.valueOf(1)));	
		
		do
		{
			E= new BigInteger(2*size, new Random());
		}
		while((E.compareTo(r)!=-1)||(E.gcd(r).compareTo(BigInteger.valueOf(1))!=0));
		
		D=E.modInverse(r);
	}
	
	public BigInteger get_p()
	{
		return (p);
	}
	
	public BigInteger get_q()
	{
		return (q);
	}
	
	public BigInteger get_r()
	{
		return (r);
	}
	
	public BigInteger get_N()
	{
		return (N);
	}
	
	public BigInteger get_E()
	{
		return (E);
	}
	
	public BigInteger get_D()
	{
		return (D);
	}
	
	public String RSAencrypt(String data)
	{
		E= new BigInteger(publicKey);
		N= new BigInteger(randomNumber);
		
		try
		{
			ciphertext = encrypt(data);
			for(int i=0;i<ciphertext.length;i++)
			{
				m[i] = ciphertext[i].intValue();
				st[i]= String.valueOf(m[i]);
				sb1.append(st[i]);
				sb1.append(" ");
				str=sb1.toString();
			}
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return str;
	}

	public BigInteger[] encrypt(String message)
	{
		int i;
		byte[] temp   = new byte[1];
		byte[] digits = new byte[8];
		
		try
		{
			digits   = message.getBytes();
			String ds= new String(digits);
						
			System.out.println("ds="+ds);
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		BigInteger[] bigdigits = new BigInteger[digits.length];
		
		for(i=0;i<bigdigits.length;i++)
		{
			temp[0]=digits[i];
			bigdigits[i]= new BigInteger(temp);				// Copying each byte from digit array into the bigdigits array			
		}
		
		BigInteger[] encrypted = new BigInteger[bigdigits.length];         
		
		for(i=0;i<bigdigits.length;i++)
		{
			encrypted[i]= bigdigits[i].modPow(E,N);			// encrypted[i] = (bigdigits[i]^E) mod N
		}
		
		return (encrypted);
	}
	
	public String RSAdecrypt()
	{
		D= new BigInteger(privateKey);
		N =new BigInteger(randomNumber);
		
		System.out.println("D="+D);
		System.out.println("N="+N);
		
		int j=0;
		StringTokenizer st = new StringTokenizer(encryptedData);
		while (st.hasMoreTokens())
		{
			s_array1[j]=st.nextToken("");
			j++;
		}
		
		BigInteger[] ciphertext1 = new BigInteger[100000];
		
		for(int i=0;i<ciphertext1.length;i++)
		{
			ciphertext1[i]= new BigInteger(s_array1[i]);
		}
		
		String recoveredPlainText = decrypt(ciphertext1,D,N,j);
		System.out.println(recoveredPlainText);
		return (recoveredPlainText);
	}
	
	public String decrypt(BigInteger[] encrypted, BigInteger D, BigInteger N, int size)
	{
		String rs="";
		BigInteger[] decrypted = new BigInteger[size];
		for(int i=0;i<decrypted.length;i++)
		{
			decrypted[i]= encrypted[i].modPow(D,N);
		}
		
		char[] charArray = new char[decrypted.length];
		byte[] byteArray = new byte[decrypted.length];
		
		for(int i=0;i<charArray.length;i++)
		{
			charArray[i] = (char) (decrypted[i].intValue());
			Integer iv = new Integer(0);
			iv = decrypted[i].intValue();
			byteArray[i] = iv.byteValue();
		}
		
		try
		{
			rs= new String(byteArray);
		}
		
		catch (Exception e)
		{
			System.out.println(e);
		}
		return (rs);
	}

	public static void main(String[] args) throws IOException
	{
		RSAlog akg = new RSAlog();
	}
		
		
		
		}
	