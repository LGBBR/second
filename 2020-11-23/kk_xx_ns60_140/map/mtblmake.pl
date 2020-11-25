#!/usr/local/bin/perl

### �g�p���W���[���̐錾
use Image::Magick;

### �ϐ��̒�`
my ($cut_image, @tbl, $infilename);
my ($name, $name1, $name2, $ext);
my ($width, $height, $size, $format);
my ($xoffset, $yoffset) = (0, 0);
my ($image1, @image2, $count);

### �Ώۉ摜�����[�v
while($infilename = glob("*\.png")){
	### �t�@�C�����Ɗg���q�𕪗�
	($name, $ext) = split(/\./, $infilename);

	### �t�@�C����ǂݍ��܂��ɉ摜�������
	($width, $height, $size, $format) = split(/\,/, Image::Magick->Ping($infilename));

	### �摜�̕��A�����m�F
	if((($width%8) != 0)||(($height%8) != 0)){
		print "���A�������W�̔{���ł͂���܂���\n";
		print "\n";
		exit 0;
	}

	print "�摜�t�H�[�}�b�g�F" . $format ."\n";
	print "�摜�T�C�Y�F" . $width ."x". $height . " " . (($width/8)*($height/8)) . "�L����\n";

	### �؂�o�����R���y�A���[�v
	print "�摜�؂�o�����R���y�A��\n";
	for($yoffset=0; $yoffset<$height; $yoffset+=8){
		for($xoffset=0; $xoffset<$width; $xoffset+=8){
			# �摜�ǂݍ���
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
				print +($count+1) . "�L�������o\r";
			}

			undef $cut_image;
		}
	}
	print "\n";

	### �e�[�u���t�@�C�������o��
	print "�e�[�u���t�@�C�������o����\n";
	open OUT, ">./" . $name . "_tbl\.txt" or die "open error:$!";

	for($yoffset=0; $yoffset<$height; $yoffset+=8){
		for($xoffset=0; $xoffset<$width; $xoffset+=8){
			printf OUT "0x%08X\,", $tbl[(($yoffset/8)*($width/8))+($xoffset/8)];
		}
		print OUT "\n";
	}
	close OUT;

	### �e�[�u���t�@�C�������o��
	print "�e�[�u���t�@�C�������o����\n";
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
