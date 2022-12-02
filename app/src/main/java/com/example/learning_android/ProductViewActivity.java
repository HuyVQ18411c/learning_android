package com.example.learning_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.learning_android.models.Product;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ProductViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_product_view);

        String data = this.getSampleData("sample.json");
        Product[] testData2 = this.mappingDataFromJsonString(data);

        String data2 = this.getSampleData("sample.xml");

        Product[] testData3 = this.mappingDataFromXmlData("");

        ListView lvProduct = (ListView) findViewById(R.id.lvProductList);

        Product[] testData = this.createTestData(10);

        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, testData3);

        lvProduct.setAdapter(adapter);

    }

    public Product[] createTestData(int len){
        int quantity = 4;
        double price = 10.123;

        Product[] sampleProductList = new Product[len];

        for(int i = 0; i < len; i++){
            String name = String.format("Product name %d", i);
            Product newProduct = new Product(name, price, quantity);
            sampleProductList[i] = newProduct;
        }
        return sampleProductList;
    }

    public String getSampleData(String filePath){
        String data = null;
        if(filePath.trim() == ""){
            filePath = "sample.json";
        }

        try{
            InputStream stream = getAssets().open(filePath);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            data = new String(buffer, "UTF-8");
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        return data;
    }

    public Product[] mappingDataFromJsonString(String data){
        Product[] mappedData = null;
        try{
            JSONArray parsedJson = new JSONArray(data);
            mappedData = new Product[parsedJson.length()];
            for(int i = 0, n = parsedJson.length(); i < n; i++){
                JSONObject product = parsedJson.getJSONObject(i);
                String name = product.getString("name");
                double price = product.getDouble("price");
                int quantity = product.getInt("quantity");

                Product newProduct = new Product(name=name, price=price, quantity=quantity);
                mappedData[i] = newProduct;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }

        return mappedData;
    }

    public Product[] mappingDataFromXmlData(String data){
        Product[] productData = null;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(getAssets().open("sample.xml"));

            doc.getDocumentElement().normalize();

            NodeList products = doc.getElementsByTagName("product");

            int n = products.getLength();
            productData = new Product[n];
            for(int i = 0 ; i < n; i++){
                Node product = products.item(i);
                if (product.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) product;

                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    double price = Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent());
                    int quantity = Integer.parseInt(eElement.getElementsByTagName("quantity").item(0).getTextContent());

                    Product newProduct = new Product(name = name, price = price, quantity = quantity);
                    productData[i] = newProduct;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

        return productData;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}