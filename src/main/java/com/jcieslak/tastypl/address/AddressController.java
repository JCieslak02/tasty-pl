package com.jcieslak.tastypl.address;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/address")
@AllArgsConstructor
public class AddressController {
    private final ModelMapper modelMapper;
    private final AddressService addressService;

    @GetMapping
    public List<AddressDto> getAddresses(){
        return addressService.getAddresses().stream().map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable(name="id") Long id){
        Address address = addressService.getAddressById(id);

        AddressDto addressResponse = modelMapper.map(address, AddressDto.class);

        return ResponseEntity.ok().body(addressResponse);
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto){
        Address addressRequest = modelMapper.map(addressDto, Address.class);

        Address address = addressService.createAddress(addressRequest);

        AddressDto addressResponse = modelMapper.map(address, AddressDto.class);

        return new ResponseEntity<>(addressResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable("addressId") Long id, @RequestBody AddressDto addressDto){
        Address addressRequest = modelMapper.map(addressDto, Address.class);

        Address address = addressService.updateAddress(id, addressRequest);

        AddressDto addressResponse = modelMapper.map(address, AddressDto.class);

        return ResponseEntity.ok().body(addressResponse);
    }
    @DeleteMapping(path="{addressId}")
    public void deleteAddress(@PathVariable("addressId") Long id){
        addressService.deleteAddress(id);
    }

}
