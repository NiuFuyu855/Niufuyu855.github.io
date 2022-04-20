// 获取数量
document.getElementById('count').innerText = relationship.dataCount;

// 自定义模式
relationship.setMode('cantonese',{
	'f':['阿爸'],
	'f,f':['阿爷'],
	'f,f,f':['太公'],
	'f,f,m':['太婆'],
	'f,m':['阿嫲'],
	'f,m,f':['太外公'],
	'f,m,m':['太外婆'],
	'f,ob':['大伯'],
	'f,ob,w':['伯娘'],
	'f,lb':['阿叔'],
	'f,lb,w':['阿婶'],
	'0,f,xb,d,s':['堂姨甥'],
	'1,f,xb,d,s':['堂外甥'],
	'0,f,xb,d,d':['堂姨甥女'],
	'1,f,xb,d,d':['堂外甥女'],
	'f,os':['姑妈'],
	'f,ls':['姑姐'],
	'f,xs,h':['姑丈'],
	'0,f,xs,d,s':['姑表姨甥'],
	'1,f,xs,d,s':['姑表外甥'],
	'0,f,xs,d,d':['姑表姨甥女'],
	'1,f,xs,d,d':['姑表外甥女'],
	'm':['阿妈','老母'],
	'm,f':['阿公'],
	'm,f,f':['外太公'],
	'm,f,m':['外太婆'],
	'm,m':['阿婆'],
	'm,m,f':['外太外公'],
	'm,m,m':['外太外婆'],
	'm,ob':['舅父'],
	'm,lb':['舅仔'],
	'm,xb,w':['舅母'],
	'0,m,xb,d,s':['舅表姨甥'],
	'1,m,xb,d,s':['舅表外甥'],
	'0,m,xb,d,d':['舅表姨甥女'],
	'1,m,xb,d,d':['舅表外甥女'],
	'm,os':['姨妈'],
	'm,ls':['姨仔'],
	'm,xs,h':['姨丈'],
	'0,m,xb,d,s':['姨姨甥'],
	'1,m,xb,d,s':['姨外甥'],
	'0,m,xb,d,d':['姨姨甥女'],
	'1,m,xb,d,d':['姨外甥女'],
	'ob':['阿哥'],
	'ob,w':['阿嫂'],
	'lb':['阿弟','细佬'],
	'lb,w':['弟妇'],
	'xb,s':['侄'],
	'xb,d':['侄女'],
	'os':['阿姐'],
	'os,w':['姐夫'],
	'ls':['阿妹'],
	'ls,w':['妹夫'],
	'0,xs,s':['姨甥'],
	'1,xs,s':['外甥'],
	'0,xs,d':['姨甥女'],
	'1,xs,d':['外甥女'],
	's':['仔'],
	's,w':['新妇'],
	's,s':['孙'],
	's,s,w':['孙新妇'],
	's,s,s':['息仔'],
	's,s,d':['息女'],
	's,d':['孙女'],
	's,d,h':['孙女婿'],
	's,d,s':['外息仔'],
	's,d,d':['外息女'],
	'd':['女'],
	'd,s':['外孙'],
	'd,s,w':['外孙新妇'],
	'd,s,s':['外息仔'],
	'd,s,d':['外息女'],
	'd,d':['外孙女'],
	'd,d,h':['外孙女婿'],
	'd,d,s':['外息仔'],
	'd,d,d':['外息女'],
});

// 标签页
(function(){
	var $module = document.querySelector('.mod-panel');
	var $items = $module.querySelectorAll('li');
	var $panels = $module.querySelectorAll('.c-panel');
	for(var i=0;i<$items.length;i++){
		(function(i){
			$items[i].addEventListener('click',function(){
				for(var j=0;j<$panels.length;j++){
					$items[j].className = i==j?'active':'';
					$panels[j].style.display = i==j?'block':'none';
				}
			});
		})(i);
	}
})();

// 通过关系找称呼
(function(){
	var $module = document.querySelector('.mod-panel');
	var $panel = $module.querySelector('.c-panel:nth-child(1)');
	var $radio = $panel.querySelectorAll('input[type="radio"]');
	var $mode = $panel.querySelectorAll('input[name="mode"]');
	var $sex = $panel.querySelectorAll('input[name="sex"]');
	var $reverse = $panel.querySelectorAll('input[name="reverse"]');
	var $small_btns = $panel.querySelectorAll('.btn-small');
	var $input = $panel.querySelector('textarea[name="input"]');
	var $result = $panel.querySelector('textarea[name="result"]');
	var toggleSex = function(sex){
		if(sex){		//男女判断
			$small_btns[2].disabled=true;
			$small_btns[3].disabled=false;
		}else{
			$small_btns[2].disabled=false;
			$small_btns[3].disabled=true;
		}
	}
	var count = function(){
		var value = $input.value.trim();
		if(value){
			var sex = $sex[0].checked?1:0;
			var mode = $mode[0].checked?$mode[0].value:$mode[1].value;
			var reverse = !$reverse[0].checked;
			var result = relationship({text:value,sex:sex,reverse:reverse,mode:mode});
			$result.value = '';
			if(result.length){
				$result.value = result.join('\n');
			}else{
				$result.value = '貌似他/她跟你不是很熟哦!';
			}
		}else{
			$result.value = '';
		}
	};
	var bindChange = function(){
		var value = $input.value.trim();
		if(value){
			var result = relationship({text:value,sex:-1,type:'chain'});
			if(result.length){
				var first_name = result[0].split('的').shift();
				var name = result[0].split('的').pop();
				if(first_name=='老公'){
					$sex[1].checked = true;
				}else if(first_name=='老婆'){
					$sex[0].checked = true;
				}
				if(!name){
					toggleSex($sex[0].checked);
				}else{
					toggleSex('爸爸,老公,儿子,哥哥,弟弟,兄弟'.indexOf(name)>-1);
				}
			}
		}else{
			$result.value = '';
		}
	};

	for(var i=0;i<$small_btns.length;i++){
		$small_btns[i].onclick = function(){
			var value = $input.value.trim();
			var name = this.getAttribute('data-value');
			if(value){
				$input.value= value+'的'+name;
			}else{
				$input.value= name;
			}
			toggleSex('爸爸,老公,儿子,哥哥,弟弟,兄弟'.indexOf(name)>-1);
		}
	}
	for(var i=0;i<$radio.length;i++){
		$radio[i].onchange=function(){
			toggleSex($sex[0].checked);
			if($result.value){
				count();
			}
		}
	}
	var hander = null;
	$input.addEventListener('paste',function(){
		hander&&clearTimeout(bindChange,250);
		hander = setTimeout(bindChange,250);
	});
	$input.addEventListener('input',function(){
		hander&&clearTimeout(bindChange,250);
		hander = setTimeout(bindChange,250);
	});
	$panel.querySelector('.btn-orange').addEventListener('click',function(){
		var value = $input.value.trim();
		var index = value.lastIndexOf('的');
		index = Math.max(0,index);
		var search = value.substr(0,index);
		$input.value = search;
		$result.value = '';
		var name = search.split('的').pop();
		if(!name){
			toggleSex($sex[0].checked);
		}else{
			toggleSex('爸爸,老公,儿子,哥哥,弟弟'.indexOf(name)>-1);
		}
	});
	$panel.querySelector('.btn-red').addEventListener('click',function(){
		$result.value = $input.value = '';
		toggleSex($sex[0].checked);
	});
	$panel.querySelector('.btn-green').addEventListener('click',count);

	toggleSex($sex[0].checked);
})();

// 通过称呼找关系
(function(){
	var $module = document.querySelector('.mod-panel');
	var $panel = $module.querySelector('.c-panel:nth-child(2)');
	var $radio = $panel.querySelectorAll('input[type="radio"]');
	var $input = $panel.querySelector('input[name="input"]');
	var $result = $panel.querySelector('textarea[name="result"]');
	var count = function(){
		var value = $input.value.trim();
		if(value){
			var result = relationship({text:value,type:'chain'});
			if(result.length){
				$result.value = result.join('\n');
			}else{
				$result.value = '貌似他/她跟你不是很熟哦!';
			}
		}else{
			$result.value = '';
		}
	};
	$panel.querySelector('.btn-red').onclick = function(){
		$result.value = $input.value = '';
	};
	$panel.querySelector('.btn-green').onclick = count;
})();

// 任意两者间称呼
(function(){
	var $module = document.querySelector('.mod-panel');
	var $panel = $module.querySelector('.c-panel:nth-child(3)');
	var $radio = $panel.querySelectorAll('input[type="radio"]');
	var $reverse = $panel.querySelectorAll('input[name="reverse2"]');
	var $person = $panel.querySelector('input[name="person"]');
	var $target = $panel.querySelector('input[name="target"]');
	var $result = $panel.querySelector('textarea[name="result"]');
	var count = function(){
		var person = $person.value.trim();
		var target = $target.value.trim();
		if(person){
			var reverse = !$reverse[1].checked;
			var result = relationship({text:person,reverse:reverse,target:target});
			if(result.length){
				$result.value = result.join('\n');
			}else{
				$result.value = '他们貌似不是很熟哦!';
			}
		}else{
			$result.value = '';
		}
	};
	for(var i=0;i<$radio.length;i++){
		$radio[i].onchange=function(){
			if($result.value){
				count();
			}
		}
	}
	$panel.querySelector('.btn-red').onclick = function(){
		$result.value = $person.value = $target.value = '';
	};
	$panel.querySelector('.btn-green').onclick = count;
})();
