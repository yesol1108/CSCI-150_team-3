<?php
	include("stringconv.php");
	function get_rsa_keys() 
	{
		$unu = gmp_init(1);
		$p = get_random_prime();
		$q = get_random_prime();
		$N = gmp_mul($p, $q);
		$fi = gmp_mul( gmp_sub($p, $unu), gmp_sub($q, $unu) );
		$e = gmp_random(4); 
		$e = gmp_nextprime($e);
		while (gmp_cmp(gmp_gcd($e, $fi), $unu) != 0)
		{
			$e = gmp_add($e, $unu);
		}
		$d = modinverse($fi, $e );
		return array($N, $d, $e);//returns the keys in an array
	}
	function modinverse ($A, $Z)// returns the mod inverse of A(modZ)
	{
		$N=$A;
		$M=$Z;
		$u1=1;
		$u2=0;
		$u3=$A;
		$v1=0;
		$v2=1;
		$v3=$Z;
		while ( gmp_cmp($v3, 0) != 0) 
		{
			$qq=gmp_div($u3,$v3);
			$t1=gmp_sub($u1, gmp_mul($qq,$v1));
			$t2=gmp_sub($u2, gmp_mul($qq,$v2));
			$t3=gmp_sub($u3, gmp_mul($qq,$v3));
			$u1=$v1;
			$u2=$v2;
			$u3=$v3;
			$v1=$t1;
			$v2=$t2;
			$v3=$t3;
			$z=1;
		}
		$uu=$u1;
		$vv=$u2;
		$zero = gmp_init(0);
		if (gmp_cmp($vv, $zero) < 0)
		{
			$I=gmp_add($vv,$A);
		}
		else
		{
			$I=$vv;
		}
		return $I;
	}
	function get_random_prime( $val = 4 ) // getting a random prime number.. gmp doesn't have a function for this.. so..
	{
		$seed = gmp_random( $val ); // get a random number
		$prime = gmp_nextprime( $seed ); // get the next prima number
		return $prime;
	}
	function rsa_encrypt($message, $public_key_d, $public_key_n) // this is easy
	{
		$message=intNum::fromstring($message);
		$message=gmp_strval($message);
		$resp = gmp_powm($message, $public_key_d, $public_key_n);
		return $resp;
	}
	function rsa_decrypt($value, $private_key_e, $public_key_n) // this one too
	{
		$resp = gmp_powm($value, $private_key_e, $public_key_n);
		$resp = intnum::fromNumber($resp);

		return $resp;
	}
?>