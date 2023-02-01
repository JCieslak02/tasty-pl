package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Address;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.repository.AddressRepository;
import com.jcieslak.tastypl.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    private AddressService addressService;

    @BeforeEach
    void setUp(){
        addressService = new AddressService(addressRepository, restaurantRepository);
    }

    @Test
    void getAddressById_shouldReturnAddress_whenIdExist() {
        // given
        Address expectedAddress = new Address(1L, "Poland", "Street", "City", "State", "Street", "2", "3");
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(expectedAddress));
        // when
        Address actualAddress = addressService.getAddressById(1L);
        // then
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    void getAddressById_shouldThrowNotFoundException_whenIdNotExist() {
        // given
        given(addressRepository.findById(anyLong())).willReturn(java.util.Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> addressService.getAddressById(1L));
    }

    @Test
    void getAllAddresses_shouldReturnAddressList(){
        // given
        Address address = new Address(
                1L,
                "Poland",
                "Mazowieckie",
                "05-600",
                "Grojec",
                "Szybka",
                "10",
                "20a"
        );
        given(addressRepository.findAll()).willReturn(List.of(address));

        List<Address> expected = List.of(address);

        // when
        List<Address> result = addressService.getAllAddresses();

        // then
        assertEquals(expected, result);
    }
    @Test
    void updateAddress_shouldReturnUpdatedAddress_whenIdExist_andIsNotOwnedByAnotherRestaurant() {
        // given
        long id = 1L;
        Address oldAddress = new Address();
        Address newAddress = new Address(
                1L,
                "Poland",
                "Mazowieckie",
                "05-600",
                "Grojec",
                "Szybka",
                "10",
                "20a"
        );

        when(addressRepository.findById(id)).thenReturn(Optional.of(oldAddress));
        when(addressRepository.save(oldAddress)).thenReturn(oldAddress);
        when(restaurantRepository.findAll()).thenReturn(List.of());

        // when
        Address updatedAddress = addressService.updateRestaurantAddress(id, newAddress);

        // then
        verify(addressRepository).save(oldAddress);
        assertEquals(newAddress.getCountry(), updatedAddress.getCountry());
        assertEquals(newAddress.getRegion(), updatedAddress.getRegion());
        assertEquals(newAddress.getZipCode(), updatedAddress.getZipCode());
        assertEquals(newAddress.getCity(), updatedAddress.getCity());
        assertEquals(newAddress.getStreet(), updatedAddress.getStreet());
        assertEquals(newAddress.getBuildingNumber(), updatedAddress.getBuildingNumber());
        assertEquals(newAddress.getSecondaryNumber(), updatedAddress.getSecondaryNumber());
    }

    @Test
    void updateAddress_shouldThrowException_whenIdDoesntExist_andIsNotOwnedByAnotherRestaurant() {
        // given
        Address newAddress = new Address();
        // then
        assertThrows(NotFoundException.class, () -> addressService.updateRestaurantAddress(30L, newAddress));
    }

    @Test
    void isAddressOwnedByAnotherRestaurant_shouldReturnFalse(){
        // given
        Address address = new Address(
                1L,
                "Poland",
                "Mazowieckie",
                "05-600",
                "Grojec",
                "Szybka",
                "10",
                "20a"
        );
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        // when
        boolean isOwned = addressService.isAddressOwnedByAnotherRestaurant(address);

        // then
        assertTrue(isOwned);

    }
}
