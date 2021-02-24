package com.kktt.jesus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JesusApplicationTests {

	@Test
	public void contextLoads() {
		String c = "The Product Description field contains an invalid value: <p><strong>Introductions:</strong><br/>Are you looking for a shelf to contain your small items? If so, you can&#39;t miss this Wood-plastic Board 4-storey locker. It adopts wood-plastic board, which is smooth and beautiful. It also features waterproof, damp proof, easy to clean and not easy to become mildewed. With hollow design, it&#39;s suitable for many occasions, like living room, hall, study, bedroom, etc. Moreover, it&#39;s easy to install with no tool needed.You can use it as a bedside cabinet / tea table / coffee table .So what are you waiting for? Just take it home!<br/></p><p><strong>Features:</strong><br/>1. Made of high quality wood-plastic board, smooth, sturdy and beautiful<br/>2. It has a long service time, durable in use<br/>3. Waterproof, damp proof and not easy to become mildewed<br/>4. Washable, easy to clean<br/>5. Can be bedside cabinet / tea table / coffee table<br/>6. Use screws to fix it on the basis of original installation can make it more fix and not easy to fall <br/></p><br/><p>Note:<br/>1.When receiving the goods, please confirm the product according to the listing of instructions. Once you find Less pieces, please provide relevant pictures to contact us. We will deal it in the time.<br/>2.Please install goods with the instruction manual.<br/>3.It is recommended that the installation should not be too tight to adjust easily. After the structure is installed, firm the interface for more stability.</p><p><strong>Specifications:</strong><br/>1. Material: Wood-plastic Board<br/>2. Color: White<br/>3. Board Thickness: 0.32&quot; / 8mm<br/>4. Product Dimensions: (15.75 x 11.02 x 19.49 )&quot; / (40 x 28 x 49.5)cm (L x W x H)</p><p><strong>Package Includes:</strong><br/>1 x Locker<br/>1 x Bag of Installation Accessories</p><p><strong>Notice:</strong><br/>1. Maintenance Method: Rinse directly with water and then wipe with cloth.<br/>2. Attention: The product size is manual measurement, which exist a little error, please refer to the physical object. Please understand!<br/></p>. The value exceeds the maximum number of characters allowed: 2000.";
		System.out.println(c.length());
	}



}

