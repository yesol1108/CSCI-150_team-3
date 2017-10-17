import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
 
class RSA
{
    private BigInteger p;						// First random number
    private BigInteger q;						// Second random number
    private BigInteger N;						// p*q
    private BigInteger phi;						// (p-1)*(q-1)
    private BigInteger e;						//	public key
    private static BigInteger d;				// private key
    private int bitlength = 100;				// sets range of random numbers (0,2^(bitLength))
    private Random     r;
 
    public RSA()
    {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }
 
    
    
    public static void main(String[] args) throws IOException
    {
    	Scanner scanner = new Scanner( System.in );
    	
        RSA rsa = new RSA();
        System.out.println("Enter information :");
        String teststring = scanner.nextLine();
    	
        System.out.println("Encrypting infromation....");
        System.out.println("Encrypting String : " + teststring);
        
        // encrypt
        byte[] encrypted = rsa.encrypt(teststring.getBytes());
        System.out.println("Encrypted as : " + new String(encrypted));
        
        // decrypt
        int test=0;
        while(test<=3)
        {
        	//Key Verification
        	System.out.println("Enter key: ");
            BigInteger key = scanner.nextBigInteger();
            System.out.println("Key="+d);
        	if(keyCheck(key))
            {
                byte[] decrypted = rsa.decrypt(encrypted);
                System.out.println("Successfully decrypted...");                
                System.out.println("Decrypted String : " + new String(decrypted));
                
                break;
            }
        	else
        	{
        		//4 Wrong attempts allowed. If still wrong process terminates
        		if(test<3)
        		{
        			test++;
        		}
        		else
        		{
        			System.out.println("Too many wrong attempts\nDecryption terminated...");
        			break;
        		}
        			
        		
        		
        	}
        		
        }
        
        
        
        
    }
 
    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for(int i=0;i<encrypted.length;i++)
        {
        	test += Byte.toString(encrypted[i]);
        }
        return test;
    }
    
    
 
    // Encrypt message
    public byte[] encrypt(byte[] message)
    {
        BigInteger n= new BigInteger(message);
        n=n.modPow(e, N);
        System.out.println("Public Key        : " + e);
        return (n.toByteArray());
    }
 
    // Decrypt message
    public byte[] decrypt(byte[] message)
    {
    		
	    	BigInteger n= new BigInteger(message);
	        n=n.modPow(d, N);
	        System.out.println("Private Key     : " + d);
	        return (n.toByteArray());	
    }
    
    public static boolean keyCheck(BigInteger i)
    {
    	return i.equals(d);
    }
}