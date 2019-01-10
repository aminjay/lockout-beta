package com.LockOut.Server.Utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.LockOut.Server.Models.Bundle;
import com.LockOut.Server.Models.Reward;
import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.BundleRepository;

public class StoreLogic {

	@Autowired
	private BundleRepository bundleRepository;
	
	public Bundle buyItem(User user, Reward ItemBought) {
		Bundle itemBundle = checkIfItemBundleExists(user.getBundles(),ItemBought);
	if(itemBundle != null) {//item exists
		if(itemBundle.getItem().getConsumeable() == 0) {//if the item is not usable you cannot have more than one
			return null;
		}
		itemBundle.setAmount(itemBundle.getAmount() +1);
		return itemBundle;
	}
	else {
		Bundle toAdd = new Bundle();
		toAdd.setAmount(1);
		toAdd.setItem(ItemBought);
		return toAdd;
	}
}


	public Bundle checkIfItemBundleExists(List<Bundle> bundles, Reward ItemBought) {
		
		for(Bundle b : bundles) {
			if(b.getItem() == ItemBought) {
				return b;
			}
		}
		return null;
	}
}