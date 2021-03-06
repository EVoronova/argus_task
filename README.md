# Тестовое задание НТЦ АРГУС: Java EE
## Стек технологий
 github, maven, java 8/java 11, wildfly, cdi, ejb, hibernate, oracle/postgres, jsf, primefaces
 
## Модель данных.

* Объекты технического учета(имеет предопределенные типы : узел, коннектор, кабель.)

* Здание(имеет составно ключ: регион, улица, номер дома)

* услуга(имеет список объектов ТУ и ссылку на владельца)

* абонент(имеет ссылку на адрес и список реализованных услуг)

* владелец услуги(имеет список услуг)

* реализованная услуга (имеет ссылку на услугу, список объектов ТУ, ссылку на абонента, также имеет статус активности даты открытия и закрытия)

* правило группового повреждения(правило, которое описывает способ получения пострадавшей абонентской базы по соотвутствующему объекту ТУ)

* групповое повреждение(имеет ссылку на правило, статус активности, даты открытия/закрытия, список абонентов, список обектов ТУ, список зданий, список реализованных услуг попавших в ГП)

## Выполненные задачи:

1. настроены структура сущностей и их взаимосвязи;

2. возможность добавления нового абонента:
   * указывается имя и адрес абонента;
   * реализована форма подбора услуг для абонента;
   * автоматическое добавление здания, и реализованной услуги в БД.

3. реализованы списочные и экранные формы:
	* список зданий; 
	* список услуг; 
	* список объектов ТУ;
   * список абонентов;
	* форма создания абонента;
	* форма списка и создания/закрытия ГП.
  
4. реализованы отчеты:
	* абоненты, попавшие в зону действия ГП более одного раза за период;
	* объекты ТУ, вышедшие из строя более одного раза за период;
	* история ГП.
  
5. реализована возможность создания ГП;
6. реализована возможность закрытия ГП; 
7. реализовано первоначальное заполнение БД(на вкладке "Дополнительно")




