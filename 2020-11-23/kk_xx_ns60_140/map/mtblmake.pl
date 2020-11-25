#!/usr/local/bin/perl

### 使用モジュールの宣言
use Image::Magick;

### 変数の定義
my ($cut_image, @tbl, $infilename);
my ($name, $name1, $name2, $ext);
my ($width, $height, $size, $format);
my ($xoffset, $yoffset) = (0, 0);
my ($image1, @image2, $count);

### 対象画像分ループ
while($infilename = glob("*\.png")){
	### ファイル名と拡張子を分離
	($name, $ext) = split(/\./, $infilename);

	### ファイルを読み込まずに画像情報を入手
	($width, $height, $size, $format) = split(/\,/, Image::Magick->Ping($infilename));

	### 画像の幅、高さ確認
	if((($width%8) != 0)||(($height%8) != 0)){
		print "幅、高さが８の倍数ではありません\n";
		print "\n";
		exit 0;
	}

	print "画像フォーマット：" . $format ."\n";
	print "画像サイズ：" . $width ."x". $height . " " . (($width/8)*($height/8)) . "キャラ\n";

	### 切り出し＆コンペアループ
	print "画像切り出し＆コンペア中\n";
	for($yoffset=0; $yoffset<$height; $yoffset+=8){
		for($xoffset=0; $xoffset<$width; $xoffset+=8){
			# 画像読み込み
			$cut_image = Image::Magick->new;
			$cut_image->Read(filename=>$infilename);
			$cut_image->Crop("8x8+$xoffset+$yoffset");
			my $quality = $cut_image->Get("quality");
			$image1 = $cut_image->ImageToBlob();

			for($count=0; $count<@image2; $count++){
				if($image1 eq $image2[$count]){
					$tbl[(($yoffset/8)*($width/8))+($xoffset/8)] = $count;
					last;
				}
			}
			if($count == @image2){
				$image2[$count] = $image1;
#				$cut_image->Write(filename=>"./" . $name . "_" . $count . "." . $ext, compress=>"None");
#				$cut_image->Write("$ext:./" . $name . "_" . $count . "." . $ext);
#				$cut_image->Write("$ext:./map_" . $count . "." . $ext);
				$tbl[(($yoffset/8)*($width/8))+($xoffset/8)] = $count;
				print +($count+1) . "キャラ抽出\r";
			}

			undef $cut_image;
		}
	}
	print "\n";

	### テーブルファイル書き出し
	print "テーブルファイル書き出し中\n";
	open OUT, ">./" . $name . "_tbl\.txt" or die "open error:$!";

	for($yoffset=0; $yoffset<$height; $yoffset+=8){
		for($xoffset=0; $xoffset<$width; $xoffset+=8){
			printf OUT "0x%08X\,", $tbl[(($yoffset/8)*($width/8))+($xoffset/8)];
		}
		print OUT "\n";
	}
	close OUT;

	### テーブルファイル書き出し
	print "テーブルファイル書き出し中\n";
	open OUT2, ">./" . $name . "_tbl\.txt2" or die "open error:$!";

	for($yoffset=0; $yoffset<$height; $yoffset+=8){
		for($xoffset=0; $xoffset<$width; $xoffset+=8){
			printf OUT2 "%08X", $tbl[(($yoffset/8)*($width/8))+($xoffset/8)];
		}
	}
	close OUT2;

}

exit 0;
__END__
