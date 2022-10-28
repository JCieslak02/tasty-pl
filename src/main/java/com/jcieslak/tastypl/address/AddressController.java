package com.jcieslak.tastypl.address;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/address")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<Address> getAddresses(){
        return addressService.getAddresses();
    }

    @PostMapping
    public void addAddress(@RequestBody Address address){
        addressService.addAddress(address);
    }

    @DeleteMapping(path="{addressId}")
    public void deleteAddress(@PathVariable("addressId") Long id){
        addressService.deleteAddress(id);
    }

    @PutMapping(path = "{addressId}")
    public void updateAddress(@PathVariable("addressId") Long id, @RequestBody Address address){
        addressService.updateAddress(id, address);
    }
}
