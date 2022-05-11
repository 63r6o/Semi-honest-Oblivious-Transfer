package SemiHonestOT.Crypto;

import java.io.IOException;
import java.math.BigInteger;

import javax.crypto.spec.DHParameterSpec;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

public class FakeKey implements ElGamalPublicKey {
    private BigInteger y;
    private transient ElGamalParameterSpec elSpec;

    public FakeKey(
        ElGamalPublicKeyParameters params)
    {
        this.y = params.getY();
        this.elSpec = new ElGamalParameterSpec(params.getParameters().getP(), params.getParameters().getG());
    }

    @Override
    public ElGamalParameterSpec getParameters() {
        return elSpec;
    }

    @Override
    public DHParameterSpec getParams() {
        return new DHParameterSpec(elSpec.getP(), elSpec.getG());
    }

    @Override
    public String getAlgorithm() {
        return "ElGamal";
    }

    @Override
    public String getFormat() {
        return "X.509";
    }

    @Override
    public byte[] getEncoded() {
        try
        {
            SubjectPublicKeyInfo    info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(elSpec.getP(), elSpec.getG())), new ASN1Integer(y));

            return info.getEncoded(ASN1Encoding.DER);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    @Override
    public BigInteger getY() {
        return y;
    }
    
}
