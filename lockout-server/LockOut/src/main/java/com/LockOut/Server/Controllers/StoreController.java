package com.LockOut.Server.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.LockOut.Server.Models.Bundle;
import com.LockOut.Server.Models.Reward;
import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.BundleRepository;
import com.LockOut.Server.Repositories.StoreRepository;
import com.LockOut.Server.Repositories.UserRepository;
import com.LockOut.Server.Utils.StoreLogic;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/store")
public class StoreController {

	// get necessary beans
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private BundleRepository bundleRepository;

	private StoreLogic Store = new StoreLogic();

	// setup calls

	/**
	 * Adds a new item
	 * @param itemName
	 * @param itemDescription
	 * @param itemCost
	 * @param consumable
	 * 	1 if consumable 0 if not
	 * @param groupID
	 * @return
	 * 	operation status
	 */
	@GetMapping(path = "/add") // Map ONLY GET Requests
	public @ResponseBody String addNewReward(@RequestParam String itemName, @RequestParam String itemDescription,
			@RequestParam int itemCost, @RequestParam int consumable, @RequestParam int groupID) {

		if(storeRepository.findByItemNameAndGroupID(itemName, groupID)!=null) {
			return "cannot create duplicate reward";
		}
		Reward r = new Reward();
		r.setItemName(itemName);
		r.setItemDescription(itemDescription);
		r.setItemCost(itemCost);
		r.setGroupID(groupID);
		r.setConsumeable(consumable);
		storeRepository.save(r);
		return "Saved";

	}

	/**
	 * Deletes an item from the store
	 * @param itemName
	 * 	item to be deleted
	 * @param groupID
	 * 	group that the item belongs to
	 * @return
	 * 	Operation status
	 */
	@GetMapping(path = "/delete") // Map ONLY GET Requests
	public @ResponseBody String deleteReward(@RequestParam String itemName, @RequestParam int groupID) {
		storeRepository.delete(storeRepository.findByItemNameAndGroupID(itemName, groupID));
		return "Deleted";
	}
	
	/**
	 * list all items in the store
	 * @return
	 * 	returns a iterable of all the rewards in the store
	 */
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Reward> getAllUsers() {
		// This returns a JSON or XML with the users
		if (storeRepository.findAll().iterator().hasNext() == true) {
			return storeRepository.findAll();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param groupID
	 * 	group ID to filter by 
	 * @return
	 * 	a list of the items from that group
	 */
	@GetMapping(path = "/findByGroupID") // Map ONLY GET Requests
	public @ResponseBody Iterable<Reward> findByGroupID(@RequestParam int groupID) {
		return storeRepository.findByGroupID(groupID);

	}
	
	
	/**
	 * 
	 * @param itemName
	 * 	item name to find
	 * @param groupID
	 * 	Group that the item is in
	 * @return
	 * 	returns the item found or null if no item found
	 */
	@GetMapping(path = "/findByItemNameAndGroupID") // Map ONLY GET Requests
	public @ResponseBody Reward findByItemNameAndGroupID(@RequestParam String itemName, @RequestParam int groupID) {
		return storeRepository.findByItemNameAndGroupID(itemName, groupID);

	}

	/**
	 * Buy an item as a user
	 * @param userName
	 * 	user name of buyer
	 * @param password
	 * 	password of buyer
	 * @param itemName
	 * 	name of item to be bought
	 * @param groupID
	 * 	group of item to be bought
	 * @return
	 * 	the item if found or null if not
	 */
	@GetMapping(path = "/buyItem") // Map ONLY GET Requests
	public @ResponseBody String buyItem(@RequestParam String userName, @RequestParam String password,
			@RequestParam String itemName, @RequestParam int groupID) {
		User buyer = userRepository.findByUserName(userName);
		Reward itemBought = storeRepository.findByItemNameAndGroupID(itemName, groupID);
		Bundle SaveBundle;
		boolean bundleExists = false;
		if (buyer != null && itemBought != null) {//check if buyer and item were found
			int pointsLeftIfBought = buyer.getPointsAvailable() - itemBought.getItemCost();
			//if the user has enough points
			if (pointsLeftIfBought >= 0) {
				buyer.setPointsAvailable(pointsLeftIfBought);
				SaveBundle = Store.buyItem(buyer, itemBought);
				//if the user is attempting to buy a non-consumable item more than once
				if(SaveBundle == null) {
					return "At max limit for this item";
				}
				//create the new bundle or update existing bundle
				bundleRepository.save(SaveBundle);
				//check bundles to see if the is new or not
				for(Bundle b : buyer.getBundles()) {
					if(b.getItem().getId() == SaveBundle.getItem().getId()) {
						bundleExists = true;
					}
				}
				//if this is the first item of this type bought update the user information
				if(!bundleExists) {
				buyer.addToBundle(SaveBundle);
				}
				
				userRepository.save(buyer);
				
				return "Success";

			} else {
				return "Insufficient Points";
			}
		} else {
			return "invalid user or item";
		}

	}

	/**
	 * 
	 * @param userName
	 * 	userName you want the items of
	 * @return
	 * 	bundles of the user with item and amount
	 */
	@GetMapping(path = "/getItemsBought") // Map ONLY GET Requests
	public @ResponseBody List<Bundle> getItemsBought(@RequestParam String userName) {
		User user = userRepository.findByUserName(userName);
		return user.getBundles();

	}
	
	/**
	 * 
	 * @param itemName
	 * 	name of item to be used
	 * @param groupID
	 * 	group of the item
	 * @param userName
	 * 	user name of the user consuming the item
	 * @param password
	 * 	password of user consuming the item
	 * @return
	 * 	operation status
	 */
	@RequestMapping(path = "/useItem") // Map ONLY GET Requests
	public @ResponseBody String useReward(@RequestParam String itemName,@RequestParam int groupID, @RequestParam String userName, @RequestParam String password) {
		User consumer = userRepository.findByUserName(userName);
		Reward item = storeRepository.findByItemNameAndGroupID(itemName, groupID);
		if((consumer == null) 
				|| !(consumer.getPassword().equals(password))) {//check credentials
			return "invalid user name or password";
			
		}
		Bundle bundle = Store.checkIfItemBundleExists(consumer.getBundles(), item);
		if(bundle == null) {
			return "You cannot use an item you do not own";
		}
		else if(bundle.getAmount() ==1) {
			List<Bundle> bundles = consumer.getBundles();
			bundles.remove(bundle);
			bundleRepository.save(bundle);
		}
		else {
			bundle.setAmount(bundle.getAmount() -1);
			bundleRepository.save(bundle);
		}
		return "Item used";
	}
}


