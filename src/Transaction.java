import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Transaction implements Serializable {

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private double amount;
    private String title;

    public Transaction(String title, double amount){
        this.amount = amount;
        this.title = title;
    }

    @Override
    public String toString(){
        return ("Transaction:" + title + '\n' + "Amount: $" + this.amount);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);
        outputStream.writeObject(this);
        outputStream.flush();
        return stream.toByteArray();
    }

}
