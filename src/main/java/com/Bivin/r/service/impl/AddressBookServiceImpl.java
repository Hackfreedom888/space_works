package com.Bivin.r.service.impl;

import com.Bivin.r.mapper.AddressBookMapper;
import com.Bivin.r.pojo.AddressBook;
import com.Bivin.r.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
